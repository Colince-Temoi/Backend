package tech.csm.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Bill implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3628988035182669861L;
	private Integer billId;
	private String cName;
	private String cPhone;
	private Date billDate;
	private Double totalAmount;
}
