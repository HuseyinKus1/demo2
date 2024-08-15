package com.fmss.propertyservice.service;

import com.fmss.propertyservice.dto.*;
import com.fmss.propertyservice.exception.PropertyNotEligibleException;
import com.fmss.propertyservice.exception.PropertyNotFoundException;
import com.fmss.propertyservice.mapper.PropertyMapper;
import com.fmss.propertyservice.model.Property;
import com.fmss.propertyservice.repository.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyService {
private final PropertyRepository propertyRepository;
private final PropertyMapper propertyMapper;
private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.listing}")
    private String listingExchange;

    @Value("${rabbitmq.routing-key.saga-event}")
    private String sagaEventsRoutingKey;

    public PropertyResponse create(CreatePropertyRequest createPropertyRequest){
        return propertyMapper.toPropertyResponse(propertyRepository.save(propertyMapper.toProperty(createPropertyRequest)));
    }

    public PropertyResponse getById(String id){
        return propertyMapper.toPropertyResponse(propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("property with requested id not found: " + id)));
    }

    public Page<PropertyResponse> getAll(Pageable pageable){
        return propertyRepository.findAll(pageable).map(propertyMapper::toPropertyResponse);
    }

    public Page<PropertyResponse> getByUserId(Long userId, Pageable pageable) {
        return propertyRepository.findAllByUserId(userId, pageable).map(propertyMapper::toPropertyResponse);
    }
    @RabbitListener(queues = "${rabbitmq.queue.property-updates}", containerFactory = "rabbitListenerContainerFactory")
    public void handlePropertyUpdate(PropertyUpdateCommand command) {
        SagaEvent sagaEvent = SagaEvent.builder().propertyId(command.getPropertyId()).sagaId(command.getSagaId()).listingId(command.getListingId()).userId(command.getUserId()).listingType(command.getListingType()).build();
        try {
            Property property = propertyRepository.findById(command.getPropertyId())
                    .orElseThrow(() -> new PropertyNotFoundException("Property not found"));

            if (!isEligible(Long.valueOf(command.getUserId()), command.getPropertyId())) {
                throw new PropertyNotEligibleException("Property is not eligible for listing");
            }
            property.setListed(true);
            propertyRepository.save(property);
            sagaEvent.setEventType("PROPERTYUPDATESUCCESS");
            rabbitTemplate.convertAndSend(listingExchange,sagaEventsRoutingKey,sagaEvent);
        } catch (Exception e) {
            log.warn("Property update failed: {}", e.getMessage());
            sagaEvent.setEventType("PROPERTYUPDATEFAILED");
            rabbitTemplate.convertAndSend(listingExchange,sagaEventsRoutingKey,sagaEvent);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.property-rollback-events}", containerFactory = "rabbitListenerContainerFactory")
    public void handlePropertyRollback(PropertyRollbackCommand propertyRollbackCommand){
        try {
         Property property = propertyRepository.findById(propertyRollbackCommand.getPropertyId()).orElseThrow(() -> new PropertyNotFoundException("not found"));
         property.setListed(false);
         propertyRepository.save(property);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
    public Boolean isEligible(Long userId, String propertyId) {
        return propertyRepository.existsByUserIdAndIdAndNotListed(userId,propertyId);
    }
}
