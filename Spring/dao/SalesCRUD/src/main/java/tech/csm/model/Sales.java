package tech.csm.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Sales implements Serializable {
	@Id
	@Column(name = "sales_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer salesId;
	@Column(name = "sales_date")
	private Date salesDate;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	@Column(name = "sales_quantity")
	private Integer salesQnty;
}
