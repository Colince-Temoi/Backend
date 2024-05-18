package tech.csm.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Country {
    private String countryId;
    private String countryName;
    private Location location;
}