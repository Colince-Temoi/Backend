package tech.csm.domain;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString @JacksonXmlRootElement
public class Location {

    private Integer locationId;
    @NotBlank(message = "Street address cannot be blank")
    @Size(min = 2, max = 100, message = "Street address must be between 2 and 100 characters")
    private String streetAddress;
    @NotBlank(message = "Postal code cannot be blank")
    @Size(min = 2, max = 10, message = "Postal code must be between 2 and 10 characters")
    private String postalCode;
    @NotBlank(message = "City cannot be blank")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;
    @NotBlank(message = "State/Province cannot be blank")
    @Size(min = 2, max = 50, message = "State/Province must be between 2 and 50 characters")
    private String stateProvince;
    
}