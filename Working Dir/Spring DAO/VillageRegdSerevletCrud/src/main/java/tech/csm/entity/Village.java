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
public class Village implements Serializable {
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name="v_id")
	private Integer villageId;
	
	private String name;
	
	private Integer population;
	
	@ManyToOne
	@JoinColumn(name="p_id")
	private Panchayat panchayat;
	
	@Column(name="auth_doc")
	private String authDoc;

}
