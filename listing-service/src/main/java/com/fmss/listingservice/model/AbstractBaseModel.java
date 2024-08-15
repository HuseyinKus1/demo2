package com.fmss.listingservice.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
//AFTER SECURITY
public abstract class AbstractBaseModel implements Serializable {
    @Id

    private ObjectId id;

    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String modifiedBy;

    @CreationTimestamp
    private Instant creationTime;

    @UpdateTimestamp
    private Instant modifiedTime;



}
