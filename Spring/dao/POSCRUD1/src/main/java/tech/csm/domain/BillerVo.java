package tech.csm.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BillerVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String billerId;
	private String billerName;
	private String phoneNumber;
	private String billDate;
	private String totalPurchaseAmount;
	
	
}
