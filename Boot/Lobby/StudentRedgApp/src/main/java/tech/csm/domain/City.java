package tech.csm.domain;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "t_city")
public class City implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "city_id")
	private Integer cityId;
	
	@Column(name = "city_name")
	private String cityName;

	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;

}
