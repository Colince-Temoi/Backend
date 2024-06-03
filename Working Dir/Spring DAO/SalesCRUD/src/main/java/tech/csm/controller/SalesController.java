package tech.csm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import tech.csm.model.Customer;
import tech.csm.model.Product;
import tech.csm.service.CustomerService;
import tech.csm.service.ProductService;

@Controller
public class SalesController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/getsales")
	public String getSalesForm(Model model) {
		
		List<Product> productList=productService.getAllProducts();
		List<Customer> customerList=customerService.getAllCustomers();
		model.addAttribute("productList", productList);
		model.addAttribute("customerList", customerList);
		return "salesform";
	}
}
