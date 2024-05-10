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
public class Customer implements Serializable {
	@Id
	@Column(name = "customer_id")
	private Integer customerId;
	@Column(name = "customer_name")
	private String customerName;
	@Column(name = "mobile_no")
	private String mobileNo;
}
