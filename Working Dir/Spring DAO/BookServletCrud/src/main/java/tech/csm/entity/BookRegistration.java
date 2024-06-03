package tech.csm.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BookRegistration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer bookId;
	private String bookTitle;
	private Double bookPrice;
	private String bookAuthorName;
	private String bookPublicationName;
	private Date bookPublicationDate;

}
