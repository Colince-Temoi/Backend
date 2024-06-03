package tech.csm.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tech.csm.domain.AdmissionDtls;
import tech.csm.domain.Collage;
import tech.csm.service.AdmissionDtlsSevice;
import tech.csm.service.CollageService;

@Controller
@RequestMapping(path = "/student")
public class MainController {

	@Autowired
	private CollageService collageService;

	@Autowired
	private AdmissionDtlsSevice admissionDtlsService;

	@GetMapping
	public String getRedgForm() {

		return "index";
	}

	@GetMapping("/getFormData")
	@ResponseBody
	public List<Collage> getCollages() {

		List<Collage> collageList = collageService.findAllCollages();
//		System.out.println(collageList);

		return collageList;
	}

	/*
	 * @RequestBody Employee jsonData expects the raw request body to contain valid
	 * JSON representing an Employee object. Spring's message converters will
	 * attempt to automatically deserialize the JSON into an Employee object for
	 * you.
	 */

	@PostMapping("/saveApplication")

	@ResponseBody
	public String saveEmployee(@RequestBody AdmissionDtls admissionDtls, Model model) {

		String msg = admissionDtlsService.saveAdmissionDtls(admissionDtls);
		System.out.println(msg);
		return msg;
	}

	@PostMapping("/cancelAdmission")
	@ResponseBody
	public String cancelAdmission(@RequestParam("admissionId") String admissionId, Model model) {
		System.out.println(admissionId);

		String msg = admissionDtlsService.cancelAdmission(Integer.parseInt(admissionId));
		return msg;
	}

//	Get All Admission Details
	@GetMapping("/getAllAdmnDtls")
	@ResponseBody
	public List<AdmissionDtls> getAllAdmissionDtls() {
//		List<AdmissionDtls> admnDtlsList = admissionDtlsService.getAllAdmissionDetatils();
		List<Map<String, Object>> admissionDtlsMap = admissionDtlsService.fetchAllAdmissionDetails();
//	
		System.out.println();
		for (Map<String, Object> map : admissionDtlsMap) {
			System.out.println(map);
		}
		return null;
//		admnDtlsList.forEach(x->System.out.println(x));
//		return admnDtlsList;

	}

}
