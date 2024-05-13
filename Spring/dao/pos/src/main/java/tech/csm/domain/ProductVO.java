package tech.csm.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductVO {
	
	private String prouctId;
	private String name;
	private String qnty;
	private String unitPrice;

}
