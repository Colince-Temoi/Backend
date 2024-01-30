package tech.csm.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TransactionVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String transactionId;
	private BillerVo billerVo;
	private ProductVo  productVo;
	private String numberofUnits;
	private String totalAmount;
}
