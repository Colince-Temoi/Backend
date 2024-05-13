package com.get_tt_right.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.get_tt_right.domain.Product;
import com.get_tt_right.domain.ProductVo;
import com.get_tt_right.domain.TransactionVo;
import com.get_tt_right.service.ProductService;
@RestController
public class PointOfSaleControllerBerrylium {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/getDataAnnBerrylium")
	public String getEmployeeData() {
    	
    	List<ProductVo> productList = productService.getAllProducts();
//    	ProductVo product = productService.getProductById(10);
    	System.out.println(productList);
    	
        // Initialize a scanner to read user input
        Scanner scanner = new Scanner(System.in);
        List<TransactionVo> transactionVos = new ArrayList<>();
        TransactionVo transactionVo = null;
        String name, phone;
        boolean continuePurchase = true;
        
        int option;
        do {
        // Display the main menu to the user
        System.out.println("-------------------------------------------------------------");
        System.out.println("1.Create Bill \n2.Show Bill\n" + "3.Exit\n" + "Enter your Choice : ");
        System.out.println("-------------------------------------------------------------");
            option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                	while (continuePurchase) {
                        System.out.print("Enter your name: ");
                        name = scanner.nextLine();

                        System.out.print("Enter your phone: ");
                        phone = scanner.nextLine();

                        displayAvailableProducts();

                        int productId = -1;
                        while (productId < 1 || productId > productList.size()) {
                            System.out.print("Enter the product ID you want to purchase: ");
                            productId = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline character

                            if (productId < 1 || productId > productList.size()) {
                                System.out.println("Invalid product ID. Please enter a valid product ID.");
                            }
                        }
                        ProductVo productVo = productService. getProductById(productId);
                        
                        int noOfUnits = -1;
                        while (noOfUnits <= 0 || noOfUnits > Integer.parseInt(productVo.getStock())) {
                            System.out.print("Enter the number of units you want to purchase: ");
                            noOfUnits = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline character

                            if (noOfUnits <= 0 || noOfUnits > Integer.parseInt(productVo.getStock())) {
                                System.out.println("Invalid number of units. Please enter a valid quantity.");
                            }
                        }
                        transactionVo = new TransactionVo();
                        transactionVo.setProductVo(productVo);
                        transactionVo.setNumberofUnits(String.valueOf(noOfUnits));
                        transactionVo.setTotalAmount(String.valueOf(Double.parseDouble(productVo.getUnitPrice()) * noOfUnits));
                        
                        transactionVos.add(transactionVo);
                        
                        System.out.print("Do you want to purchase more [Y/N]? ");
                        char choice = scanner.next().charAt(0);
                        scanner.nextLine(); // Consume the newline character

                        if (choice == 'N') {
                            continuePurchase = false;
                        }
                        
                	}
                	transactionVos.forEach(x->System.out.println(x));
                    break;
                case 2:
                    // Show bill logic
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (option != 3);
scanner.close();
return "Working";
    }
        private void displayAvailableProducts() {
        	List<ProductVo> productList = productService.getAllProducts();
            System.out.println("Available Products:");
            for (int i = 0; i < productList.size(); i++) {
                ProductVo productVo = productList.get(i);
                System.out.println(productVo);
            }
        }
}