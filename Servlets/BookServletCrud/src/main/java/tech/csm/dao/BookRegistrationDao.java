package tech.csm.dao;

import java.util.List;

import tech.csm.entity.BookRegistration;

public interface BookRegistrationDao {

	String saveBook(BookRegistration bookRegistration);

	List<BookRegistration> getAllBooks();

}
