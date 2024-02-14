package tech.csm.domain;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class State implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "state_id")
	private Integer stateId;
	
	@Column(name = "state_name")
	private String stateName;
	
	@OneToMany(mappedBy = "state")
	private List<City> cities;
	
	@OneToMany(mappedBy = "state")
	private List<Address> addresses;
	
}
