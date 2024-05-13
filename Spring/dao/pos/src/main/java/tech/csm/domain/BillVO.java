package tech.csm.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BillVO {
	private String billId;
	private String cName;
	private String cPhone;
	private String billDate;
	private String totalAmount;

}
