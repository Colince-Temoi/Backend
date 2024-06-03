package tech.csm.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "t_insurance_dtls")
public class Farmer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "farmer_id")
	private Integer farmerInsuranceId;
	
	@Column(name = "aadhar_id")
	private Integer aadharNumber;
	@Column(name = "farmer_name")
	private String farmerName;
	@Column(name = "father_name")
	private String fatherName;
	@Column(name = "address_id")
	private String addressNumber;
	
	@ManyToOne
	@JoinColumn(name = "crop_id")
	private Crop crop;
//	Category

}
