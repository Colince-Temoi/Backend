package tech.csm.domain;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class Country {

    @NotBlank(message = "Country ID cannot be blank")
    private String countryId;
    @NotBlank(message = "Country name cannot be blank")
    @Size(min = 2, max = 50, message = "Country name must be between 2 and 50 characters")
    private String countryName;
    @NotNull(message = "Location cannot be null")
    @Valid
    private Location location;
}