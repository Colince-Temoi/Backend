package tech.csm.domain;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
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

@Setter
@Getter
@ToString
@Entity
@Table(name = "t_address_master")
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "address_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer addressId;

	@Column(name = "lane")
	private String lane;

	@Column(name = "zip")
	private String zip;

	@ManyToOne()
	@JoinColumn(name = "state_id")
	private State state;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "roll_no")
	private Student student;
}
