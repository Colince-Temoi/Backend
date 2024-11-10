package com.get_tt_right.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A DTO representing a response.
 * Mentioning @AllArgsConstructor will create a constructor with all the fields in the class as arguments.
 * By default, the @Data annotation will not generate a constructor with all arguments present in the class.
 * With these 2 fields my client can get to know whether a given operation was successful or not.
 */
@Data @AllArgsConstructor
public class ResponseDto {
    private String statusCode;
    private String statusMsg;
}