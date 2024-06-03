package tech.csm.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "t_insurance")
public class Insurance implements Serializable {
	@Id
	@Column(name = "insurance_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer insuranceId;
	
	@ManyToOne
	@JoinColumn(name = "crop_id")
	private Crop crop;
	
	@Column(name = "farmer_name")
	private String farmerName;
	
	@Column(name = "aadhaar_no")
	private Integer aadhaarNo;
	
	@Column(name = "father_name")
	private String fatherName;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "category")
	private String farmerCategory;
}
