package tech.csm.ServletLobby;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ThirdServlet
 */
@WebServlet("/third")
public class ThirdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		PrintWriter pw = response.getWriter();

		ServletContext context = getServletContext();

		String url = context.getInitParameter("url");

		String name = getInitParameter("name");
		
//		Accessing all Cookies of this application: At the moment I have just created one named; ck1
		Cookie[] cookie =request.getCookies();
		pw.println("Cookie name: "+cookie[0].getName()+"<br>");
		pw.println("Cookie Value: "+cookie[0].getValue()+"<br>");
		
		pw.println("Output from Third<br>");
		pw.println("------------------------<br>");
		pw.println("Accessing Context data from third: " + url);
		pw.println("<br>Accessing data present in request object from third: " + request.getAttribute("location"));
		pw.println("<br>Init||private data: " + name + "<br>");
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
