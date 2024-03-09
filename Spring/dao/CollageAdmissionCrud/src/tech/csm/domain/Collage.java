package tech.csm.domain;

import java.io.Serializable;

public class Collage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	Primitive dependencies
	private Integer collageId;
	private String collageName;
	private String collageAddress;
	private Integer noOfSeats;

//	Secondary dependencies

//	Getters and Setters

	public Integer getCollageId() {
		return collageId;
	}

	public void setCollageId(Integer collageId) {
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

	public Integer getNoOfSeats() {
		return noOfSeats;
	}

	public void setNoOfSeats(Integer noOfSeats) {
		this.noOfSeats = noOfSeats;
	}

//	toString
	@Override
	public String toString() {
		return "Collage [collageId=" + collageId + ", collageName=" + collageName + ", collageAddress=" + collageAddress
				+ ", noOfSeats=" + noOfSeats + "]";
	}

}
