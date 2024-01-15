package tech.csm.service;

import java.util.List;

import tech.csm.entity.BookRegistrationVo;

public interface BookRegistrationService {

	String saveBook(BookRegistrationVo bookRegistrationVo);

	List<BookRegistrationVo> getAllBooks();

}
