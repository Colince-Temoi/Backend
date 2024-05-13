package tech.csm.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Product implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4185544315133422419L;
	private Integer prouctId;
	private String name;
	private Integer qnty;
	private Double unitPrice;
}
