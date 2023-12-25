package tech.csm.productcrud.domain;

public class ProductVo {

	private String productId;
	private String productName;
	private String unitPrice;
	private String manufacturingDate;

	public ProductVo(String productId, String productName, String unitPrice, String manufacturingDate) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.manufacturingDate = manufacturingDate;
	}

	public ProductVo() {
		// TODO Auto-generated constructor stub
	}

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

	public String getManufacturingDate() {
		return manufacturingDate;
	}

	public void setManufacturingDate(String manufacturingDate) {
		this.manufacturingDate = manufacturingDate;
	}

	@Override
	public String toString() {
//		You can customize this to print things in your preffered style.
		return "ProductVo [productId=" + productId + ", productName=" + productName + ", unitPrice=" + unitPrice
				+ ", manufacturingDate=" + manufacturingDate + "]";
	}

}
