package com.get_tt_right.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "Response",
        description = "Schema to hold successful response information"
)
@Data @AllArgsConstructor
public class ResponseDto {
    /** Here I'm not mentioning any example values because this is a generic response schema which can be used by multiple REST APIs.
     * I mean the response contents can change based on the REST API. So it's not a good idea to provide example values here.
     * The same thing also applies to the ErrorResponseDto class.
     * But if you have such requirements to provide examples, then you can do that .
     * */
    @Schema(
            description = "Status code in the response"
    )
    private String statusCode;

    @Schema(
            description = "Status message in the response"
    )
    private String statusMsg;
}