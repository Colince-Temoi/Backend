package tech.csm.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import tech.csm.entity.Customer;
import tech.csm.entity.Product;
import tech.csm.entity.Sale;
import tech.csm.service.CustomerService;
import tech.csm.service.ProductService;
import tech.csm.service.SaleService;

@Controller
public class MainController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private SaleService saleService;
	

	@GetMapping("/getSalesForm")
	public String getRedgForm(Model model) {
		
		List<Customer> customerList = customerService.getAllCustomers();
		List<Product> productList = productService.getAllProducts();
		
		model.addAttribute("customerList", customerList);
		model.addAttribute("productList", productList);
		
		return "salesform";
	}
	
	@PostMapping("/saveSale")
	public String saveSale(@ModelAttribute Sale sale) {
		/*Set Sales entity with some data
		 * 
		 * We need to create a Sales object? Spring Container will take care of that.
		 * We need to set our inputs to the Created entity object? @ModelAttribute is binding our Form data with all possible attributes of our Sales entity.
		 * In this case, @ModelAttribute is behind the scenes setting for us:
		    primitive attributes
		    --------------------
		      1. noOfUnits
		      
		    Secondary attributes
		    --------------------
		      1.customer
		      2.product
		 *It is doing so because these attributes we have them as input parameters in our form from which the /saveSale request is coming.
		 *Whatever is not coming from the form. i.e., salesDate and salesId, these 2 we need to set them explicitly
		 *Here we will only set the salesDate, salesId will be auto-generated from the DB hence no need to worry about it.
		 *This is how to go about this situation given a scenario where you are getting data from a form in Spring boot.
		 *NOTE: THE ATTRIBUTES WHICH WE ARE SETTING, THEIR RESPECTIVE INPUT FIELDS name ATTRIBUTE SHOULD MATCH!!! WHEN UTILIZING @ModelAttribute ANNOTATION.
		 */	
		 sale.setSalesDate(new Date());
		 
//			Test to see that the data is coming
//			System.out.println(sale);
		
//		Now pass this data to service layer
		Sale sale1 = saleService.saveSale(sale);
		
		
//		Test to verify that the saved sale is being returned to us
//		System.out.println(sale1);
		

		return "redirect:./getSalesForm";
	}
}
