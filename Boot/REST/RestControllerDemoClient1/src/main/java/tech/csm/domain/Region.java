package tech.csm.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Region {
    private String regionName;
    private Country country;
}