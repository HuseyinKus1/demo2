package com.fmss.api_gateway.model;

import lombok.*;
import org.slf4j.Marker;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "application_logs")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    @Setter
    @Id
    private String id;
    //TODO esneklestir
    private String data;
    @Setter
    private String message;
    @Setter
    private String executionTime;
    @Setter
    private String level;
    @Setter
    private String logger;
    @Setter
    private String name;
    @Setter
    private String thread;
    @Setter
    private String httpStatus;
    @Setter
    private String correlationId;
    @Setter
    private Instant timeStamp;
    @Setter
    private Marker marker;

}
