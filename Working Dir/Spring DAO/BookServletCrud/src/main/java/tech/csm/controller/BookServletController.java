package tech.csm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import tech.csm.util.DBUtil;

/**
 * Servlet implementation class BookServletController
 */
public class BookServletController extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		// TODO Auto-generated method stub
		doGet(request, response);
		System.out.println("You saved!");
		Map<String, String[]> paramMap = request.getParameterMap();
		for (Entry<String, String[]> entry : paramMap.entrySet()) {

			System.out.println("key: " + entry.getKey() + " Value :" + Arrays.toString(entry.getValue()));

		}

	}

}
