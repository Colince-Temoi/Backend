package tech.csm.ServletLobby;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SecondServlet
 */
public class SecondServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");

		PrintWriter pw = response.getWriter();

		ServletContext context = getServletContext();
		

		String url = context.getInitParameter("url");
		String name = getInitParameter("name");
		
		pw.println("Output from second<br>");
		pw.println("------------------------<br>");
		pw.println("Accessing Context data from second: " + url);
		pw.println("<br>Accessing data present in request object from second: " + request.getAttribute("location"));
		pw.println("<br>Init||private data: " + name+"<br>");
		
//		Modifying request object
		request.setAttribute("location", "Nairobi,Kenya");
		
		RequestDispatcher rd = request.getRequestDispatcher("/third");
		rd.include(request, response);
		
//		response.getWriter().append("Served at: ").append(request.getContextPath());
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
