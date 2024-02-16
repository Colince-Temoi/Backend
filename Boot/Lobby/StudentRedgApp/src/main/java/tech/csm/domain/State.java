package tech.csm.domain;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "t_state")
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
