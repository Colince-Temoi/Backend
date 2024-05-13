package tech.csm.ServletLobby;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		
//		Accessing all Cookies of this application: At the moment I have just created one named; ck1
//		Cookie[] cookie =request.getCookies();
		
//		Modifying Cookie(s) content(s)
//		cookie[0].setValue("YourCookie");
		
//		Attaching the modified cookie to response object : If I don't do this, the modified changes can only be be utilized in this end-point
//		Other end-points i.e.; /third will only access the unmodified changes.
//		response.addCookie(cookie[0]);
		
//		Hover over this method to see what it does
		HttpSession ses = request.getSession(false);
		
		System.out.println(ses.getId()+"+++++");
		System.out.println(ses.getAttribute("myses"));
		
//		pw.println("Cookie name: "+cookie[0].getName()+"<br>");
//		pw.println("Cookie Value: "+cookie[0].getValue()+"<br>");
		
		
		pw.println("<br>Output from second<br>");
		pw.println("------------------------<br>");
		pw.println("Accessing Context data from second: " + url);
		pw.println("<br>Accessing data present in request object from second: " + request.getAttribute("location"));
		pw.println("<br>Init||private data: " + name+"<br>");
		
//		Modifying request object
//		request.setAttribute("location", "Nairobi,Kenya");
//		
//		RequestDispatcher rd = request.getRequestDispatcher("/third");
//		rd.include(request, response);
		
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
