package com.fmss.listingservice.service;

import com.fmss.listingservice.constants.ExceptionConstants;
import com.fmss.listingservice.dto.*;
import com.fmss.listingservice.dto.command.ListingRollbackCommand;
import com.fmss.listingservice.dto.command.ListingStatusUpdateCommand;
import com.fmss.listingservice.dto.event.PropertyUpdateEvent;
import com.fmss.listingservice.dto.event.SagaEvent;
import com.fmss.listingservice.exception.ListingException;
import com.fmss.listingservice.exception.ResourceNotFoundException;
import com.fmss.listingservice.feign.PropertyClient;
import com.fmss.listingservice.feign.UserClient;
import com.fmss.listingservice.mapper.ListingMapper;
import com.fmss.listingservice.model.Listing;
import com.fmss.listingservice.model.ListingStatus;
import com.fmss.listingservice.repository.ListingRepository;
import com.fmss.listingservice.util.CurrentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingService {
    private final ListingRepository listingRepository;
    private final ListingMapper listingMapper;
    private final UserClient userClient;
    private final PropertyClient propertyClient;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.listing}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key.property-update}")
    private String propertyUpdateRoutingKey;

    @Value("${rabbitmq.routing-key.user-update}")
    private String userUpdateRoutingKey;

    @Value("${rabbitmq.routing-key.saga-event}")
    private String listingSagaRoutingKey;


    public ListingResponse createListing1(CreateListingRequest createListingRequest) {
        Listing listing = listingRepository.save(listingMapper.toListing(createListingRequest));
        String correlationId = CurrentRequest.getCorrelationId();
        SagaEvent sagaEvent = SagaEvent.builder().eventType("LISTINGCREATED").sagaId(correlationId).userId(CurrentRequest.getUserId().toString())
                .propertyId(listing.getPropertyId()).listingId(listing.getId()).listingType(listing.getListingType()).build();
        rabbitTemplate.convertAndSend(exchangeName, listingSagaRoutingKey, sagaEvent);
        return listingMapper.toListingResponse(listing);
    }

    public ListingResponse update(UpdateListingRequest updateListingRequest) {
        Listing listing = listingRepository.findById(updateListingRequest.getId()).orElseThrow(()
                -> new ListingException(ExceptionConstants.LISTING_NOT_FOUND));
        listing.setInfo(updateListingRequest.getInfo());
        listing.setPrice(updateListingRequest.getPrice());
        listing.setTitle(updateListingRequest.getTitle());
        return listingMapper.toListingResponse(listingRepository.save(listing));
    }

    public ListingResponse updateStatus(UpdateListingStatus updateListingStatus) {
        Listing listing = listingRepository.findByIdAndAndUserId(updateListingStatus.getId(), CurrentRequest.getUserId())
                .orElseThrow(() -> new ListingException(ExceptionConstants.LISTING_NOT_FOUND));
        if (listing.getListingStatus().equals(ListingStatus.IN_PREVIEW) && !CurrentRequest.isAdmin()) {
            throw new ListingException(HttpStatus.UNAUTHORIZED.toString());
        }
        listing.setListingStatus(updateListingStatus.getListingStatus());
        return listingMapper.toListingResponse(listingRepository.save(listing));
    }
    @RabbitListener(queues = "${rabbitmq.queue.listing-events}", containerFactory = "rabbitListenerContainerFactory")
    public void handlePropertyUpdate(ListingRollbackCommand command) {
      try{ listingRepository.delete(listingRepository.findById(command.getListingId()).orElseThrow(() -> new ListingException(ExceptionConstants.LISTING_NOT_FOUND)));

       log.info("listing rolled back");}

      catch (Exception e){
          log.error(e.getMessage());
      }
    }
    @RabbitListener(queues = "${rabbitmq.queue.listing-status-events}", containerFactory = "rabbitListenerContainerFactory")
    public void handleListingStatusUpdate(ListingStatusUpdateCommand listingStatusUpdateCommand){
        try{
           Listing listing = listingRepository.findById(listingStatusUpdateCommand.getListingId()).orElseThrow(()
                   -> new ResourceNotFoundException(ExceptionConstants.LISTING_NOT_FOUND));
           listing.setListingStatus(ListingStatus.valueOf(listingStatusUpdateCommand.getStatus()));
           listingRepository.save(listing);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public boolean delete(String id) {
        Listing listing = listingRepository.findById(id).orElseThrow(() -> new ListingException(ExceptionConstants.LISTING_NOT_FOUND));
        if (CurrentRequest.getUserId().equals(listing.getUserId()) || CurrentRequest.isAdmin()) {
            PropertyUpdateEvent event = new PropertyUpdateEvent(listing.getPropertyId(), false);
            rabbitTemplate.convertAndSend(exchangeName, propertyUpdateRoutingKey, event);
            listingRepository.delete(listing);
            return true;
        } else throw new ListingException(ExceptionConstants.USER_NOT_ELIGIBLE);
    }

    public Page<ListingResponse> getAllActive(Pageable pageable) {
        return listingRepository.findAll(pageable).map(listingMapper::toListingResponse);
    }

    public Page<ListingResponse> getAllByUser(Pageable pageable) {
        Long userId = CurrentRequest.getUserId();
        return listingRepository.findAllByUserId(pageable, userId).map(listingMapper::toListingResponse);
    }

    public Page<ListingResponse> getAllByUserIdAndStatus(Pageable pageable, ListingStatus listingStatus) {
        Long userId = CurrentRequest.getUserId();
        return listingRepository.findAllByUserIdAndListingStatus(pageable, userId, listingStatus.toString())
                .map(listingMapper::toListingResponse);
    }
}
