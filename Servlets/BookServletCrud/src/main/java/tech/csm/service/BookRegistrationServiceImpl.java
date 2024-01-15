package tech.csm.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
	private BookRegistration convertFromVoToDto(BookRegistrationVo bookRegistrationVo) {
		bookRegistration.setBookAuthorName(bookRegistrationVo.getBookAuthorName());
		bookRegistration.setBookPrice(Double.parseDouble(bookRegistrationVo.getBookPrice()));
		bookRegistration.setBookPublicationName(bookRegistrationVo.getBookPublicationName());
		bookRegistration.setBookTitle(bookRegistrationVo.getBookTitle());
		try {
			bookRegistration.setBookPublicationDate(
					new SimpleDateFormat("yyyy-MM-dd").parse(bookRegistrationVo.getBookPublicationDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookRegistration;

	}

	@Override
	public List<BookRegistrationVo> getAllBooks() {
		List<BookRegistration> bookRegistrations = bookRegistrationDao.getAllBooks();

//		Create a container to store the processed data-Vo type data
		List<BookRegistrationVo> bookRegistrationVos = new ArrayList<>();
		;
		BookRegistrationVo bookRegistrationVo = null;

//		Convert from Dto to Vo type
		for (BookRegistration bookRegistration : bookRegistrations) {
			bookRegistrationVo = new BookRegistrationVo();
			bookRegistrationVo = convertFromDtoToVo(bookRegistration);
			bookRegistrationVos.add(bookRegistrationVo);
		}

		return bookRegistrationVos;
	}

	private BookRegistrationVo convertFromDtoToVo(BookRegistration bookRegistration) {
//		Create a container to store Book Vo's
		BookRegistrationVo bookRegistrationVo = new BookRegistrationVo();

//		Perform the conversions
		bookRegistrationVo.setBookAuthorName(bookRegistration.getBookAuthorName());
		bookRegistrationVo.setBookId(bookRegistration.getBookId().toString());
		bookRegistrationVo.setBookPrice(bookRegistration.getBookPrice().toString());
		bookRegistrationVo.setBookPublicationDate(
				new SimpleDateFormat("dd/MM/yyyy").format(bookRegistration.getBookPublicationDate()));
		bookRegistrationVo.setBookPublicationName(bookRegistration.getBookPublicationName());
		bookRegistrationVo.setBookTitle(bookRegistration.getBookTitle());

//		Finally return the processed data.
		return bookRegistrationVo;

	}

}
