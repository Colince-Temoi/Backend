package tech.csm.ServletLobby;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class FirstServlet
 */
public class FirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getCo
		response.setContentType("text/html");

		PrintWriter pw = response.getWriter();

		ServletContext context = getServletContext();

		String url = context.getInitParameter("url");

		String name = getInitParameter("name");
		
		request.setAttribute("location", "Nairobi");

//		RequestDispatcher rd = request.getRequestDispatcher("/second");
//		rd.include(request, response);
		
//		Creating cookie
//		Cookie ck = new Cookie("ck1", "Mycookie");
		
//		Setting some max time limit.
//		ck.setMaxAge(10);
		
//		Adding Cookie to response object
//		response.addCookie(ck);
		
		
		
//		Creating HttpSession object
		HttpSession ses = request.getSession();
		
//		Setting some values into HttpSession object
		ses.setAttribute("myses", "Colince");
		
		System.out.println(ses.getId());
		
		pw.println("----------Inside first--------<br>");

		pw.println("Accessing Context data from first: " + url);
		pw.println("<br>Init-private-data: " + name);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
