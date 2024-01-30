package tech.csm.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// This annotation is a combination of: @Setter, @Getter, @toSetring,...etc
@Setter
@Getter
@ToString
public class ProductVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String stock;
	private String unitPrice;
}
