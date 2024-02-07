package tech.csm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
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
		List<Sale> salesList = saleService.getAllSales();
		
		model.addAttribute("customerList", customerList);
		model.addAttribute("productList", productList);
		model.addAttribute("salesList", salesList);
		
		return "salesform";
	}
	
	@PostMapping("/saveSale")
	public String saveSale(@ModelAttribute Sale sale) {

//		Set date-Reason: This is not coming from the form.
		 sale.setSalesDate(new Date());
		 
//			Test to see that the data is coming
//			System.out.println(sale);
		
//		Now pass this data to service layer
		Sale sale1 = saleService.saveSale(sale);
		
		
//		Test to verify that the saved sale is being returned to us
//		System.out.println(sale1);
		

		return "redirect:./getSalesForm";
	}
	
	@GetMapping("/getQuantityByProductId")
	public void getQuantityByProductId(@RequestParam("productId") Integer productId,  HttpServletResponse resp) throws IOException {
//		Get the Product
		Product product = productService.getProduct(productId);	
//		Sending the the response object back to the AJAX client
		resp.getWriter().print(product.getProductStock());
		
	}
	@GetMapping("/delSale")
	public String delSale(@RequestParam("sid") Integer sid) {
//		Invoke Service method to delete a Sale
		Sale sale = saleService.deleteSale(sid);
		return "redirect:./getSalesForm";
	}
	
	@GetMapping("/getSaleToUpdateById")
	public String getSaleToUpdateById(@RequestParam("sid") Integer sid, Model model) {
//		Invoke Service method to get the Sale data you need to update
		Sale saleToUpdate = saleService.getSaletoUpdate(sid);
		
//		Send this Sale back to the form
		model.addAttribute("saleToUpdate", saleToUpdate);
		
//		Send back all the necessary data that need to be on the form so that things don't break up. Nothing but we are using all the code we have in /getSalesForm end-point
		List<Customer> customerList = customerService.getAllCustomers();
		List<Product> productList = productService.getAllProducts();
		List<Sale> salesList = saleService.getAllSales();
		
		model.addAttribute("customerList", customerList);
		model.addAttribute("productList", productList);
		model.addAttribute("salesList", salesList);
		
		return "salesform";
	}
	
	
}
