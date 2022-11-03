package com.minimi.domain.user.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorForm {
    private String message;
    private String errorCode;
    private int statusCode;

}
