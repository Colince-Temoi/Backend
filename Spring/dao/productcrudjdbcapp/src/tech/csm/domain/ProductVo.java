package tech.csm.domain;

public class ProductVo {
	private String productId;
	private String productName;
	private String unitPrice;
	private String mfgDate;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getMfgDate() {
		return mfgDate;
	}

	public void setMfgDate(String mfgDate) {
		this.mfgDate = mfgDate;
	}

	@Override
	public String toString() {
		return "ProductVo [productId=" + productId + ", productName=" + productName + ", unitPrice=" + unitPrice
				+ ", mfgDate=" + mfgDate + "]";
	}

}
