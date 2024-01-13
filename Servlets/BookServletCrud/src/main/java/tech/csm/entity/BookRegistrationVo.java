package tech.csm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookRegistrationVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bookId;
	private String bookTitle;
	private String bookPrice;
	private String bookAuthorName;
	private String bookPublicationName;
	private String bookPublicationDate;

}
