package tech.csm.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Collage implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonProperty("collage_id")
	private Integer collageId;

	@JsonProperty("collage_name")
	private String collageName;
	
	@JsonProperty("collage_total_seats")
	private Integer totalSeats;
	
	@JsonProperty("course_fee")
	private Double courseFee;
	
	
	
}
