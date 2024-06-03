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
@Table(name="purchase")
public class Purchase implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchasse_id")
	private Integer purchaseId;
	@Column(name = "customer_name")
	private String customerName;
	@Column(name = "customer_phone_no")
	private String customerPhoneNo;

	@ManyToOne
	@JoinColumn(name = "c_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "p_id")
	private Product product;
}
