package tech.csm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString @AllArgsConstructor @NoArgsConstructor
public class Location {
	private Integer locationId;
    private String streetAddress;
    private String postalCode;
    private String city;
    private String stateProvince;
}
