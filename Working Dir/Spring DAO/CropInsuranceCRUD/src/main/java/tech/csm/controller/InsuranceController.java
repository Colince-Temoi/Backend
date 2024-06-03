package tech.csm.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import tech.csm.model.Season;
import tech.csm.model.Crop;
import tech.csm.model.Insurance;
import tech.csm.service.SeasonService;
import tech.csm.service.CropService;
import tech.csm.service.InsuranceService;

@Controller
public class InsuranceController {
	
	@Autowired
	private CropService cropService;
	@Autowired
	private SeasonService seasonService;
	@Autowired
	private InsuranceService insuranceService;
	
	@GetMapping("/getinsurance")
	public String getInsuranceForm(Model model) {
		
		List<Season> seasonList=seasonService.getAllSeasons();
		List<Insurance> insuranceList=insuranceService.getAllInsurance();
		
		model.addAttribute("seasonList", seasonList);
		model.addAttribute("insuranceList",insuranceList);
		return "insuranceform";
	}
	@PostMapping("/saveInsurance")
	public String saveInsurance(@ModelAttribute Insurance insurance, RedirectAttributes rd) {
		Insurance i=insuranceService.saveInsurance(insurance);	
		rd.addFlashAttribute("msg", "one insurance saved successfully with id: "+i.getInsuranceId());
		return "redirect:./getinsurance";
		
	}
	@GetMapping("/getCropsBySeasonId")
	public void getCropsBySeasonId(@RequestParam("seasonId") Integer seasonId,HttpServletResponse resp) throws IOException {
		List<Crop> cropsList=cropService.getCropBySeasonId(seasonId);
		StringBuilder out = new StringBuilder("<option value=\"0\">-select-</option>");

		cropsList.forEach(crop -> {
		    out.append(String.format("<option value=\"%s\">%s</option>", crop.getCropId(), crop.getCropName()));
		});
		System.out.println(out.toString());
		resp.getWriter().print(out);
	}
	
	@GetMapping("/checkIfAadharExistsForCrop")
	public void checkIfAadharExistsForCrop(@RequestParam("aadhaarNo") Integer aadhaarNo, @RequestParam("cropId") Integer cropId, HttpServletResponse resp) throws IOException {
		boolean exists=insuranceService.aadhaarNoexistsByCropId(cropId, aadhaarNo);
		String out = (exists)? "1" : "0";
		resp.getWriter().print(out);
	}
	
	@GetMapping("/delInsurance")
	public String deleteInsuranceById(@RequestParam("insId") Integer insuranceId, RedirectAttributes rd) {
		String msg=insuranceService.deleteInsuranceById(insuranceId);
		rd.addFlashAttribute("msg", msg);
		
		return "redirect:./getinsurance";
	}
	
	
	@GetMapping("/upInsurance")
	public String updateInsuranceById(Model model,@RequestParam("insId") Integer insuranceId) {
		Insurance insurance=insuranceService.getInsuranceById(insuranceId);		
		List<Season> seasonList=seasonService.getAllSeasons();
		List<Insurance> insuranceList=insuranceService.getAllInsurance();
		List<Crop> cropsList=cropService.getCropBySeasonId(insurance.getCrop().getSeason().getSeasonId());
		
		model.addAttribute("uinsurance", insurance);
		model.addAttribute("seasonList", seasonList);
		model.addAttribute("insuranceList",insuranceList);
		model.addAttribute("cropsList",cropsList);
		return "insuranceform";
	}
	
	
}
