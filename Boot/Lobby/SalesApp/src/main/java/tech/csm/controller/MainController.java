package tech.csm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import tech.csm.entity.Customer;
import tech.csm.entity.Product;
import tech.csm.entity.Sales;
import tech.csm.service.CustomerService;
import tech.csm.service.ProductService;

@Controller
public class MainController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;
	

	@GetMapping("/getSalesForm")
	public String getRedgForm(Model model) {
		
		List<Customer> customerList = customerService.getAllCustomers();
		List<Product> productList = productService.getAllProducts();
		
		model.addAttribute("customerList", customerList);
		model.addAttribute("productList", productList);
		
		return "salesform";
	}
	
	@PostMapping("/saveSale")
	public void saveSale(@ModelAttribute Sales sales) {
		
//		Test to see that the data is coming
		System.out.println(sales);
	}
}
