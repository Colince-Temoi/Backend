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

import tech.csm.entity.Block;
import tech.csm.entity.Panchayat;
import tech.csm.entity.Village;
import tech.csm.service.VillageService;
import tech.csm.service.VillageServiceImpl;
import tech.csm.util.FileUploadUtil;

@MultipartConfig(fileSizeThreshold = 1024*1024*5, maxFileSize = 1024*1024*10)
public class SaveVlg extends HttpServlet {
	
	private VillageService villageService;
	public SaveVlg() {
		villageService=new VillageServiceImpl();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Village v=new Village();
		Part part = req.getPart("adoc");
		v.setName(req.getParameter("vName").trim());
		v.setPopulation(Integer.parseInt(req.getParameter("vPop")));
		v.setAuthDoc(FileUploadUtil.uploadFile(part));
		Panchayat p=new Panchayat();
		p.setPanchayatId(Integer.parseInt(req.getParameter("panchayatId")));
		v.setPanchayat(p);
		
		String msg=villageService.saveVillage(v);
		
		resp.sendRedirect("./getRegdForm");
			
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
