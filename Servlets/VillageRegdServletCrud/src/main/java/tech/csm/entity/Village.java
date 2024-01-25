package tech.csm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
