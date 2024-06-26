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
@Entity
@Getter
@Setter
@Table(name="t_village")
public class Village implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "village_id")
	private Integer villageId;
	@Column(name = "village_name")
	private String villageName;
	@Column(name = "no_of_people")
	private Integer noOfPeople;

	@ManyToOne
	@JoinColumn(name = "panchayart_id")
	private Panchayart panchayart;
	
	@ManyToOne
	@JoinColumn(name = "block_id")
	private Block block;
	

	@Override
	public String toString() {
		return "Village [villageId=" + villageId + ", villageName=" + villageName + ", noOfPeople=" + noOfPeople
				+ ", panchayart=" + panchayart.getPanchayartName() + ", block=" + block.getBlockname() + "]";
	}

}
