package tech.csm.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import tech.csm.entity.BookRegistration;
import tech.csm.util.DBUtil;

public class BookRegistartionDaoImpl implements BookRegistrationDao {
//	Primitive dependencies

//	Secondary dependencies
	private Session ses;

	public BookRegistartionDaoImpl() {
		ses = DBUtil.getSessionFactory().openSession();
	}

	@Override
	public String saveBook(BookRegistration bookRegistration) {
		Transaction transaction = ses.beginTransaction();
		ses.persist(bookRegistration);
		
		String msg = "1 book saved with id: "+ses.getIdentifier(bookRegistration);
		transaction.commit();
		ses.close();
		return msg;
	}

}
