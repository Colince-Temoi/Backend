package tech.csm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "t_book")
public class BookRegistration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private Integer bookId;
	@Column(name = "title")
	private String bookTitle;
	@Column(name = "price")
	private Double bookPrice;
	@Column(name = "aurthor_name")
	private String bookAuthorName;
	@Column(name = "publication_name")
	private String bookPublicationName;
	@Column(name = "publication_date")
	private Date bookPublicationDate;

}
