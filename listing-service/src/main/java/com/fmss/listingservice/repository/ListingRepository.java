package com.fmss.listingservice.repository;


import com.fmss.listingservice.model.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListingRepository extends MongoRepository<Listing, String> {

    Optional<Listing> findByIdAndAndUserId(String id, Long userId);

    Page<Listing> findAll(Pageable pageable);

    Page<Listing> findAllByUserId(Pageable pageable, Long userId);

    Page<Listing> findAllByUserIdAndListingStatus(Pageable pageable, Long userId, String listingstatus);
}
