package tech.csm.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InsuranceController {
	@GetMapping("/getInsuranceDetails")
	public String getSalesForm(Model model) {

//		List<Product> productList = productService.getAllProducts();
//		List<Customer> customerList = customerService.getAllCustomers();
//		List<Sales> salesList = salesService.getAllSales();

//		model.addAttribute("productList", productList);
//		model.addAttribute("customerList", customerList);
//		model.addAttribute("salesList", salesList);
		return "salesform";
	}
}
