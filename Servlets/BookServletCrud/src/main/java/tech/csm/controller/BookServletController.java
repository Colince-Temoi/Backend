package tech.csm.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import tech.csm.entity.BookRegistrationVo;
import tech.csm.service.BookRegistrationService;
import tech.csm.service.BookRegistrationServiceImpl;
import tech.csm.util.DBUtil;

/**
 * Servlet implementation class BookServletController
 */
public class BookServletController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
//	Primitive dependencies
	
//	Secondary dependencies
	private BookRegistrationService bookRegistrationService;
	
	public BookServletController() {
		bookRegistrationService = new BookRegistrationServiceImpl();
	}
	

	@Override
	public void init() throws ServletException {
		DBUtil.getSessionFactory();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");

		PrintWriter pw = response.getWriter();

		pw.println(DBUtil.getSessionFactory().openSession());

		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter PrintWriter = response.getWriter();
		
		// Using getParameterMap to get query string key associated values
		/*
		 * System.out.println("You saved!"); Map<String, String[]> paramMap =
		 * request.getParameterMap(); for (Entry<String, String[]> entry :
		 * paramMap.entrySet()) {
		 * 
		 * System.out.println("key: " + entry.getKey() + " Value :" +
		 * Arrays.toString(entry.getValue()));
		 * 
		 * }
		 */
//		Creating an object to store request query string inputs
		BookRegistrationVo bookRegistrationVo = new BookRegistrationVo();

		bookRegistrationVo.setBookAuthorName(request.getParameter("anid"));
		bookRegistrationVo.setBookPrice(request.getParameter("bpriceid"));
		bookRegistrationVo.setBookPublicationDate(request.getParameter("pdid"));
		bookRegistrationVo.setBookPublicationName(request.getParameter("pnid"));
		bookRegistrationVo.setBookTitle(request.getParameter("btid"));
		
		String msg = bookRegistrationService.saveBook(bookRegistrationVo);
		
//		System.out.println(msg);
		
		PrintWriter.println("<h2>"+msg+"</h2>");
		PrintWriter.println("<a href='http://localhost:8080/BookServletCrud/' class='d-block'>Home</a>");

//		System.out.println(bookRegistrationVo);

		doGet(request, response);

	}

}
