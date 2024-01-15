package tech.csm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import tech.csm.entity.BookRegistration;
import tech.csm.util.DBUtil;

public class BookRegistartionDaoImpl implements BookRegistrationDao {
//	Primitive dependencies

//	Secondary dependencies
	private Session ses;

	@Override
	public String saveBook(BookRegistration bookRegistration) {
//		Create Session
		ses = DBUtil.getSessionFactory().openSession();
		Transaction transaction = ses.beginTransaction();// applies for DML: Insert,Update and Delete
		ses.persist(bookRegistration);

		String msg = "1 book saved with id: " + ses.getIdentifier(bookRegistration);
		transaction.commit();// applies for DML: Insert,Update and Delete
		ses.close();
		return msg;
	}

	@Override
	public List<BookRegistration> getAllBooks() {
//		Create Session
		ses = DBUtil.getSessionFactory().openSession();
//		Prepare query
		final String seQuery = "FROM BookRegistration";
//	    Create a container to store the list of Books
		List<BookRegistration> bookList = null;

//		Same as PreparesStatement in Jdbc
		Query<BookRegistration> q = ses.createQuery(seQuery);

//		Same as the logic that executes the query and iterates over the ResultSet to return some output.
		bookList = q.list();

		ses.close();

		return bookList;
	}

}
