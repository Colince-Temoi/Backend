package tech.csm.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CityVo {
	
	private String cityId;
	private String cityName;
	private StateVo stateVo;
}
