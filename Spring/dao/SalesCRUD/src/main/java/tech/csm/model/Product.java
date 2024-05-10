package tech.csm.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Product implements Serializable {
	@Id
	@Column(name = "product_id")
	private Integer productId;
	@Column(name = "product_name")
	private String productName;
	
	private Integer quantity;
	@Column(name = "unit_price")
	private Double unitPrice;
}
