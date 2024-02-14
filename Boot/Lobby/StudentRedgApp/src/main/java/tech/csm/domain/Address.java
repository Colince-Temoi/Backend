package tech.csm.domain;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Address implements Serializable {

	@Id
	@Column(name = "address_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer addressId;
	
	private String lane;
	
	@ManyToOne()
	@JoinColumn(name = "state_id")
	private State state;
	
	@Column(name = "zip")
	private String zip;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "roll_no")
	private Student student;
}
