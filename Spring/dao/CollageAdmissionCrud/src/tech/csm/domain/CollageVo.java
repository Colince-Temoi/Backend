package tech.csm.domain;

import java.io.Serializable;

public class CollageVo implements Serializable {

	private static final long serialVersionUID = 1L;

//	Primitive dependencies
	private String collageId;
	private String collageName;
	private String collageAddress;
	private String noOfSeats;

//	Secondary dependencies

//	Getters and setters
	public String getCollageId() {
		return collageId;
	}

	public void setCollageId(String collageId) {
		this.collageId = collageId;
	}

	public String getCollageName() {
		return collageName;
	}

	public void setCollageName(String collageName) {
		this.collageName = collageName;
	}

	public String getCollageAddress() {
		return collageAddress;
	}

	public void setCollageAddress(String collageAddress) {
		this.collageAddress = collageAddress;
	}

	public String getNoOfSeats() {
		return noOfSeats;
	}

	public void setNoOfSeats(String noOfSeats) {
		this.noOfSeats = noOfSeats;
	}

//	toString
	@Override
	public String toString() {
		return "CollageVo [collageId=" + collageId + ", collageName=" + collageName + ", collageAddress="
				+ collageAddress + ", noOfSeats=" + noOfSeats + "]";
	}

}
