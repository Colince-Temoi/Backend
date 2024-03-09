package tech.csm.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdmissionDtls implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("enrollment_id")
	private int enrollmentId;

	@JsonProperty("applicany_name")
	private String applicanyName;
	
	@JsonProperty("fourth_optional")
	private String fourthOptional;	

	@JsonProperty("enrollment_date")
	private LocalDate enrollmentDate;

	private Collage collage;
}
