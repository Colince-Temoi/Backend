package tech.csm.entity;

import java.io.Serializable;

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
@Table(name = "t_villages")
public class Village implements Serializable {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name="village_id")
	private Integer villageId;
	
	@Column(name="village_name")
	private String name;
	
	private Integer population;
	
	@ManyToOne
	@JoinColumn(name="constituency_id")
	private Constituency constituency;
	
	@Column(name="authorization_doc")
	private String authDoc;

}
