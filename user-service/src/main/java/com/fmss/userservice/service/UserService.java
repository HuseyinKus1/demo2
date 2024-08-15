package com.fmss.userservice.service;


import com.fmss.userservice.client.PaymentClient;
import com.fmss.userservice.constants.ExceptionConstants;
import com.fmss.userservice.dto.event.SagaEvent;
import com.fmss.userservice.dto.response.UserPackagesResponse;
import com.fmss.userservice.dto.request.PaymentRequest;
import com.fmss.userservice.dto.request.RegisterUserRequest;
import com.fmss.userservice.dto.request.UpdateUserRequest;
import com.fmss.userservice.dto.response.BaseUserResponse;
import com.fmss.userservice.dto.response.PaymentResponse;
import com.fmss.userservice.exception.ResourceNotFoundException;
import com.fmss.userservice.exception.TargetAlreadyExistsException;
import com.fmss.userservice.helper.LockWrapper;
import com.fmss.userservice.mapper.UserMapper;
import com.fmss.userservice.mapper.UserPackagesMapper;
import com.fmss.userservice.model.Packages;
import com.fmss.userservice.model.User;
import com.fmss.userservice.model.UserPackages;
import com.fmss.userservice.model.enums.PackageType;
import com.fmss.userservice.repository.PackagesRepository;
import com.fmss.userservice.repository.UserPackagesRepository;
import com.fmss.userservice.repository.UserRepository;
import com.fmss.userservice.util.CurrentUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PackagesRepository packagesRepository;
    private final UserPackagesRepository userPackagesRepository;
    private final UserPackagesMapper userPackagesMapper;
    private final ConcurrentHashMap<Long, LockWrapper> userLocks = new ConcurrentHashMap<>();
    private final PaymentClient paymentClient;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.listing}")
    private String listingExchange;

    @Value("${rabbitmq.routing-key.saga-event}")
    private String sagaEventsRoutingKey;

    public User create(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByUsername(registerUserRequest.getUsername())) {
            throw new TargetAlreadyExistsException(ExceptionConstants.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new TargetAlreadyExistsException(ExceptionConstants.EMAIL_ALREADY_EXISTS);
        }
        return userRepository.save(userMapper.toUser(registerUserRequest));
    }

    public Page<BaseUserResponse> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toBaseUserResponse);
    }

    public String deleteById(Long id) {
        userRepository.deleteById(id);
        return "user deleted";
    }

    public BaseUserResponse update(UpdateUserRequest updateUserRequest) {
        Optional<User> userOptional = userRepository.findById(CurrentUser.getPrincipal().getId());
        if (userOptional.isPresent()) {
            User user = userRepository.save(userMapper.updateUser(userOptional.get(), updateUserRequest));
            return userMapper.toBaseUserResponse(user);
        } else {
            throw new ResourceNotFoundException(ExceptionConstants.USER_NOT_FOUND);
        }
    }

    public BaseUserResponse addBalance(BigDecimal amount) {
        User user = userRepository.findById(CurrentUser.getPrincipal().getId()).orElseThrow(()
                -> new ResourceNotFoundException(ExceptionConstants.USER_NOT_FOUND));
        user.setBalance(user.getBalance().add(amount));
        return userMapper.toBaseUserResponse(userRepository.save(user));
    }

    @Transactional
    public String buyPackages(PackageType type) {
        User user = userRepository.findById(CurrentUser.getPrincipal().getId())
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionConstants.USER_NOT_FOUND));

        LockWrapper lockWrapper = userLocks.compute(user.getId(), (key, existingLockWrapper)
                -> existingLockWrapper == null ? new LockWrapper() : existingLockWrapper);
        lockWrapper.updateLastAccessTime();

        if (!lockWrapper.getLock().tryLock()) {
            throw new RuntimeException("user purchase is already processing");
        }
        Packages packageToBuy = packagesRepository.findByType(type)
                .orElseThrow(() -> new RuntimeException("Package type not found"));
        UserPackages userPackages = userPackagesRepository.findByUserIdAndPackageType(user.getId(), type.toString());
        try {
            PaymentRequest paymentRequest = new PaymentRequest(user.getId(), user.getBalance(),
                    packageToBuy.getPrice(), packageToBuy.getId());
            PaymentResponse paymentResponse = paymentClient.processPayment(paymentRequest).getBody();
            if (!Objects.requireNonNull(paymentResponse).getStatus().equals("success")) {
                throw new RuntimeException("payment is not successful");
            }

            if (userPackages == null) {
                userPackages = userPackagesMapper.toUserPackages(user, packageToBuy);
            } else {
                boolean packageExists = userPackages.getPackages().getType().equals(type);

                if (packageExists) {
                    userPackages.setExpirationDate(userPackages.getExpirationDate().plus(packageToBuy.getValidityDays(), ChronoUnit.DAYS));
                    userPackages.setRemainingListings(userPackages.getRemainingListings() + packageToBuy.getRightsToListing());
                    user.setBalance(user.getBalance().subtract(packageToBuy.getPrice()));
                } else {
                    userPackages.setPackages(packageToBuy);
                    if (userPackages.getExpirationDate().isBefore(Instant.now())) {
                        userPackages.setExpirationDate(Instant.now().plus(packageToBuy.getValidityDays(), ChronoUnit.DAYS));
                        userPackages.setRemainingListings(packageToBuy.getRightsToListing());
                    } else {
                        userPackages.setExpirationDate(userPackages.getExpirationDate().plus(packageToBuy.getValidityDays(), ChronoUnit.DAYS));
                        userPackages.setRemainingListings(userPackages.getRemainingListings() + packageToBuy.getRightsToListing());
                    }
                }
            }
            userPackagesRepository.save(userPackages);
            return "User bought/renewed package of type: " + type;
        } finally {
            lockWrapper.getLock().unlock();
        }
    }

    @Transactional
    public String decreaseListings(PackageType type) {
        UserPackages userPackages = userPackagesRepository.findByUserIdAndPackageTypeOptional(CurrentUser.getPrincipal().getId(),
                type.toString()).orElseThrow(()
                -> new ResourceNotFoundException("user has no such package"));
        userPackages.setRemainingListings(userPackages.getRemainingListings() - 1);
        return "decreased available listings";
    }

    @Scheduled(cron = "0 */10 * * * ?", zone = "UTC")
    public void lockReleaser() {
        long currentTime = System.currentTimeMillis();
        userLocks.entrySet().removeIf(entry ->
                currentTime - entry.getValue().getLastAccessTime() > TimeUnit.MINUTES.toMillis(10));
    }
    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.user-events}", containerFactory = "rabbitListenerContainerFactory")
    public void handleUserUpdate(SagaEvent event) {
        try{
        User user = userRepository.findById(Long.valueOf(event.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionConstants.USER_NOT_FOUND));
        UserPackages userPackages = userPackagesRepository.findByUserIdAndPackageTypeOptional(user.getId(),
                event.getListingType()).orElseThrow(()
                -> new ResourceNotFoundException(ExceptionConstants.RESOURCE_NOT_FOUND));
        if (userPackages.getRemainingListings() >= 1) {
            userPackages.setRemainingListings(userPackages.getRemainingListings() - 1);
            event.setEventType("USERUPDATESUCCESS");

        } else {
            throw new ResourceNotFoundException(ExceptionConstants.INSUFFICIENT_PACKAGES);
        }

        rabbitTemplate.convertAndSend(listingExchange,sagaEventsRoutingKey,event);

        }catch (Exception e){
            log.error(e.getMessage());
            event.setEventType("USERUPDATEFAILED");
            rabbitTemplate.convertAndSend(listingExchange,sagaEventsRoutingKey,event);
        }

    }

    public Boolean isEligible(String userId, String listingType) {
        UserPackages userPackages = userPackagesRepository.findByUserIdAndPackageTypeOptional(Long.valueOf(userId), listingType).orElseThrow(()
                -> new ResourceNotFoundException("user has no such"));
        return userPackages.getRemainingListings() >= 1;
    }

    public Page<UserPackagesResponse> getUsersPackages(Pageable pageable) {
        return userPackagesRepository.findByUserIdAndPageable(CurrentUser.getPrincipal().getId(), pageable)
                .map(userPackagesMapper::toUserPackagesResponse);
    }
}





