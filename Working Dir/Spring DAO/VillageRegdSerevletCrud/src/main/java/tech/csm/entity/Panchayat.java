package tech.csm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Panchayat implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name="p_id")
	private Integer panchayatId;
	
	private String name;
	
	@ManyToOne
	@JoinColumn(name="block_id")
	private Block block;
	
	
	

}
