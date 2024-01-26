package tech.csm.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DownloadController extends HttpServlet{

	private final int ARBITARY_SIZE = 1048;
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		The Path shows location to file server
		String fileName = "C:\\Users\\Colince.temoi\\Desktop\\Home\\FileServer\\" + req.getParameter("fileName");
		
		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-disposition", "attachment;filename=" + req.getParameter("fileName"));
		
		try (FileInputStream in = new FileInputStream(fileName);
				OutputStream out = resp.getOutputStream()) {
			
	            byte[] buffer = new byte[ARBITARY_SIZE];	            
	            int numBytesRead;
	            while ((numBytesRead = in.read(buffer)) > 0) {
	                out.write(buffer, 0, numBytesRead);
	            }
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
