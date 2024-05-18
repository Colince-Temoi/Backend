package tech.csm.domain;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class Region {

    private Integer regionId;
    
    @NotBlank(message = "Region name cannot be blank")
    @Size(min = 2, max = 50, message = "Region name must be between 2 and 50 characters")
    private String regionName;
    
    @NotNull(message = "Country cannot be null")
    @Valid
    private Country country;
}