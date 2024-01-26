package tech.csm.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import tech.csm.entity.Constituency;
import tech.csm.entity.Village;
import tech.csm.service.VillageService;
import tech.csm.service.VillageServiceImpl;
import tech.csm.util.FileUploadUtil;

@MultipartConfig(fileSizeThreshold = 1024*1024*5, maxFileSize = 1024*1024*10)
public class SaveVillage extends HttpServlet {
	
	VillageService villageService;
	
	public SaveVillage() {
		villageService = new VillageServiceImpl();
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Create store to store Village data
		Village v=new Village();
		
//		Getting the uploaded file
		Part part = req.getPart("adoc");
		
//		Set the information in request object into our Village store
		v.setName(req.getParameter("vName").trim());
		v.setPopulation(Integer.parseInt(req.getParameter("vPop")));
		v.setAuthDoc(FileUploadUtil.uploadFile(part));
//		Create object to store Constituency Information
		Constituency p=new Constituency();
		p.setConstituencyId(Integer.parseInt(req.getParameter("constituency")));
		v.setConstituency(p);
		
		String msg=villageService.saveVillage(v);
		
		System.out.println(msg);
		
		resp.sendRedirect("./");
		
//		System.out.println(part.getSubmittedFileName());
//		Writing the file into file server by invoking File Upload Util class behaviour
//		String fileName = FileUploadUtil.uploadFile(part);
//		System.out.println(fileName);
//		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
