package com.fmss.propertyservice.repository;

import com.fmss.propertyservice.model.Property;
import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends MongoRepository<Property,String> {

    Page<Property> findAll(Pageable pageable);

    Page<Property> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "{ 'userId': ?0, '_id': ?1, 'isListed': false }", exists = true)
    boolean existsByUserIdAndIdAndNotListed(Long userId, String id);
}
