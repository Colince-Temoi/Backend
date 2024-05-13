package tech.csm.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BillProductVO {

	private String tId;
	private BillVO billVo;
	private ProductVO productVo;
	private String noOfUnits;
}
