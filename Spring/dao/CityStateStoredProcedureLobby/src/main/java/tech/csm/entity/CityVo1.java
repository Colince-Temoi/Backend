package tech.csm.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CityVo1 {
	
	private String cityId;
	private String cityName;
	private StateVo1 stateVo;
}
