package tech.csm.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import tech.csm.entity.Constituency;
import tech.csm.entity.County;
import tech.csm.entity.Village;
import tech.csm.service.ConstituencyService;
import tech.csm.service.CountyService;
import tech.csm.service.VillageService;

@Controller
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 5, maxFileSize = 1024 * 1024 * 1024 * 10)
public class MainController {

	@Autowired
	private CountyService countyService;

	@Autowired
	private VillageService villageService;

	@Autowired
	private ConstituencyService constituencyService;

	@GetMapping("/getRedgForm")
	public String getRedgForm(Model model) {

//		Invoke CountyServiceImpl behaviours
		List<County> countyList = countyService.getAllCounties();

//		List<Constituency> constituencyList = constituencyService.getAllConstituencies(1);

//		Getting all Villages
		List<Village> villageList = villageService.getAllVillages();

//		Attach the objects to req resp object -Model object- so that it will be available for us in the jsp page
		model.addAttribute("countyList", countyList);
		model.addAttribute("villageList", villageList);
		return "redgForm";
	}

//	This end-point will be invoked by an Ajax call behind the scenes
	@GetMapping("/getConstituenciesByCountyId")
	public void getConstituenciesByCountyId(Model model, HttpServletResponse httpRes,
			@RequestParam("countyId") Integer cId) {

		List<Constituency> constituencyList = constituencyService.getAllConstituencies(cId);

//		System.out.println(constituencyList);

		String res = "<option value='0'>-select-</option>";
		for (Constituency c : constituencyList) {
			res += "<option value='" + c.getConstituencyId() + "'>" + c.getName() + "</option>";
		}

		try {
			httpRes.getWriter().print(res);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		System.out.println(res);

		model.addAttribute("countyList", constituencyList);

	}
	
	@PostMapping("/saveVlg")
	public String saveVillage(HttpServletRequest httpReq) {
		
		System.out.println("Hello Vilage");
		
//		Local Variables with increased scope
		Part part=null;
		
//		Create store to store Village data
		Village v=new Village();
		
//		Getting the uploaded file
		try {
			part = httpReq.getPart("adoc");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Set the information in request object into our Village store
		v.setName(httpReq.getParameter("vName").trim());
		v.setPopulation(Integer.parseInt(httpReq.getParameter("vPop")));
		
//		File upload logic
		try {
			InputStream is = part.getInputStream();
			byte[] allData = is.readAllBytes();
			FileOutputStream fos = new FileOutputStream("C:\\Users\\Colince.temoi\\Desktop\\Home\\FileServer\\" + part.getSubmittedFileName());
			fos.write(allData);
			is.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		You will place this File upload logic in some util class and you will just call the method like below.
		
//		v.setAuthDoc(FileUploadUtil.uploadFile(part));
		
		v.setAuthDoc(part.getSubmittedFileName());
		
//		Create object to store Constituency Information
		Constituency p=new Constituency();
		p.setConstituencyId(Integer.parseInt(httpReq.getParameter("constituency")));
		v.setConstituency(p);

		
		
		String msg=villageService.saveVillage(v);
		
		System.out.println(msg);
		
//		Spring way to perform redirection.
		
		return "redirect:./getRedgForm";
	}
	
}
