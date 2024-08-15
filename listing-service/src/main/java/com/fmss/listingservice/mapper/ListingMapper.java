package com.fmss.listingservice.mapper;


import com.fmss.listingservice.dto.CreateListingRequest;
import com.fmss.listingservice.dto.ListingResponse;
import com.fmss.listingservice.model.Listing;
import com.fmss.listingservice.model.ListingStatus;
import com.fmss.listingservice.util.CurrentRequest;
import org.springframework.stereotype.Component;

@Component
//@Mapper(implementationName = "ListingMapperImpl" , componentModel = "spring")
public class ListingMapper {
    public Listing toListing(CreateListingRequest createListingRequest){
        Listing.ListingBuilder listing = Listing.builder();
        listing.userId(CurrentRequest.getUserId());
        listing.propertyId(createListingRequest.getPropertyId());
        listing.title(createListingRequest.getTitle());
        listing.info(createListingRequest.getInfo());
        listing.listingStatus(ListingStatus.IN_PREVIEW);
        listing.listingType(createListingRequest.getListingType());
        return listing.build();
    }
    public ListingResponse toListingResponse(Listing listing){
        ListingResponse.ListingResponseBuilder listingResponse = ListingResponse.builder();
        listingResponse.id(listing.getId());
        listingResponse.userId(listing.getUserId());
        listingResponse.propertyId(listing.getPropertyId());
        listingResponse.info(listing.getInfo());
        listingResponse.title(listing.getTitle());
        listingResponse.listingStatus(listing.getListingStatus());
        listingResponse.listingType(listing.getListingType());
        return listingResponse.build();


    }






}
