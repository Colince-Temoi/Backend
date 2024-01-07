package tech.csm.domain;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3872384540758232571L;
	private Integer productId;
	private String productName;
	private Double unitPrice;
	private Date mfgDate;

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

	public Date getMfgDate() {
		return mfgDate;
	}

	public void setMfgDate(Date mfgDate) {
		this.mfgDate = mfgDate;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", unitPrice=" + unitPrice
				+ ", mfgDate=" + mfgDate + "]";
	}

}
