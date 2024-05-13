package tech.csm.productcrud.domain;

import java.util.Date;

public class Product {

	private Integer productId;
	private String productName;
	private Double unitPrice;
	private Date manufacturingDate;

	public Product(Integer productId, String productName, Double unitPrice, Date manufacturingDate) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.manufacturingDate = manufacturingDate;
	}

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Date getManufacturingDate() {
		return manufacturingDate;
	}

	public void setManufacturingDate(Date manufacturingDate) {
		this.manufacturingDate = manufacturingDate;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", unitPrice=" + unitPrice
				+ ", manufacturingDate=" + manufacturingDate + "]";
	}

}
