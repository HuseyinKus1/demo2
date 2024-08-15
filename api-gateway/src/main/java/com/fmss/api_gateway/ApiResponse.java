package com.fmss.api_gateway;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDateTime;

@Getter
@Setter
@Slf4j
public class ApiResponse<T> implements CommandLineRunner {
    private String status;
    private T data;
    private String message;
    private String timestamp;

    public ApiResponse(String status, T data, String message){
        this.timestamp = timeFormat(LocalDateTime.now());
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String timeFormat(LocalDateTime localDateTime){
        return localDateTime.toString().replace("T"," ").substring(0,19);
    }

    @Override
    public void run(String... args){
        log.info(log.getClass().toString());
    }
}
