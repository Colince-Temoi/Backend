package tech.csm.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import tech.csm.dao.BookRegistartionDaoImpl;
import tech.csm.dao.BookRegistrationDao;
import tech.csm.entity.BookRegistration;
import tech.csm.entity.BookRegistrationVo;

public class BookRegistrationServiceImpl implements BookRegistrationService {

//	Primitive dependencies

//	Secondary Dependencies
	private BookRegistrationDao bookRegistrationDao;
	private BookRegistration bookRegistration;

	public BookRegistrationServiceImpl() {
		bookRegistrationDao = new BookRegistartionDaoImpl();
		bookRegistration = new BookRegistration();
	}

	@Override
	public String saveBook(BookRegistrationVo bookRegistrationVo) {

//		Convert the Vo object to Dto
		BookRegistration bookRegistration = convertFromVoToDto(bookRegistrationVo);

//		Invoke dao layer methods
		String msg = bookRegistrationDao.saveBook(bookRegistration);
		return msg;
	}

//	Logic to convert from Vo to Dto
	public BookRegistration convertFromVoToDto(BookRegistrationVo bookRegistrationVo) {
		bookRegistration.setBookAuthorName(bookRegistrationVo.getBookAuthorName());
		bookRegistration.setBookPrice(Double.parseDouble(bookRegistrationVo.getBookPrice()));
		bookRegistration.setBookPublicationName(bookRegistrationVo.getBookPublicationName());
		bookRegistration.setBookTitle(bookRegistrationVo.getBookTitle());
		try {
			bookRegistration.setBookPublicationDate(new SimpleDateFormat("yyyy-MM-dd").parse(bookRegistrationVo.getBookPublicationDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookRegistration;

	}

}
