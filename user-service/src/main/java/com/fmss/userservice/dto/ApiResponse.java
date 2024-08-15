package com.fmss.userservice.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiResponse <T>{
    private String status;
    private T data;
    private String message;
    private String timestamp;

    public ApiResponse(String status,T data, String message){
        this.timestamp = timeFormat(LocalDateTime.now());
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String timeFormat(LocalDateTime localDateTime){
        return localDateTime.toString().replace("T"," ").substring(0,19);
    }
}
