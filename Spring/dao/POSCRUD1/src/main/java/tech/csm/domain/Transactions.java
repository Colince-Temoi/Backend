package tech.csm.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Transactions implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer transactionId;
	private Biller billerVo;
	private Product product;
	private Integer numberofUnits;
	private Double totalAmount;
}
