package tech.csm.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class City {
	private Integer cityId;
	private String cityName;
	private State state;
}
