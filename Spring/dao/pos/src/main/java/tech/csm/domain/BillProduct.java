package tech.csm.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BillProduct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -69842792201954468L;
	private Integer tId;
	private Bill bill;
	private Product product;
	private Integer noOfUnits;
}
