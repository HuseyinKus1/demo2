package com.hk.orchestratorservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sagas")
public class Saga {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String sagaId;
    private String eventType;
    private String listingId;
    private String userId;

    @CreationTimestamp
    private Instant creationTime;
    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;
}
