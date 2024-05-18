package tech.csm.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class ApiErrorResponse {
	 private HttpStatus status;
	    private String message;
	    private String errorCode;
	    private long timestamp;
	    private String developerMessage;

	    // Constructor with all fields
	    public ApiErrorResponse(HttpStatus status, String message, String errorCode, String developerMessage) {
	        this.status = status;
	        this.message = message;
	        this.errorCode = errorCode;
	        this.timestamp = System.currentTimeMillis();
	        this.developerMessage = developerMessage;
	    }
}
