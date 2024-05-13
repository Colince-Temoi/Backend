package com.get_tt_right.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter@Getter@ToString
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer productId;
	private String productName;
	private Integer stock;
	private Double unitPrice;
	
}
