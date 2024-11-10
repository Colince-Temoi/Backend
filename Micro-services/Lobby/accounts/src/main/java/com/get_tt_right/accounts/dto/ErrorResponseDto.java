package com.get_tt_right.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * A DTO representing an error response.
 * This we can use whenever there is an error/exception in our application.
 * @Field apiPath: The path of the API where the error occurred.
 * @Field errorCode: The error code.
 * @Field errorMessage: The error message.
 * @Field errorTime: The time when the error occurred.
 * This will help us to debug the error/logs easily.
 */
@Data @AllArgsConstructor
public class ErrorResponseDto {
    private  String apiPath;
    private HttpStatus errorCode;
    private  String errorMessage;
    private LocalDateTime errorTime;

}