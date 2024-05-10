package com.get_tt_right.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.get_tt_right.domain.BillerVo;
import com.get_tt_right.domain.Product;
import com.get_tt_right.domain.ProductVo;
import com.get_tt_right.domain.TransactionVo;
import com.get_tt_right.service.BillerService;
import com.get_tt_right.service.ProductService;
@RestController
public class PointOfSaleController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BillerService billerService;
	
	@GetMapping("/getDataAnn")
	public String getEmployeeData() {
    	
    	List<ProductVo> productList = productService.getAllProducts();
    	ProductVo product = productService.getProductById(10);
    	
    	System.out.println(productList);
    	
        // Initialize a scanner to read user input
        Scanner scanner = new Scanner(System.in);
        
        int option;
        do {
        // Display the main menu to the user
        System.out.println("-------------------------------------------------------------");
        System.out.println("1.Create Bill \n2.Show Bill\n" + "3.Exit\n" + "Enter your Choice : ");
        System.out.println("-------------------------------------------------------------");
            option = Integer.parseInt(scanner.nextLine());

            switch (option) {

             case 1: // Create Bill Logic 
            	 BillerVo billerVo = new BillerVo(); 
            	 List<TransactionVo> transactions = new ArrayList<>();
                System.out.print("Enter your name: ");
                billerVo.setBillerName(scanner.nextLine());

                System.out.print("Enter your phone number: ");
                billerVo.setPhoneNumber(scanner.nextLine());
                
                System.out.println("Available Products:");
                for (ProductVo p : productList) {
                    System.out.println("ID: " + p.getId() + ", Name: " + p.getName() + ", Stock: " + p.getStock() + ", Unit Price: $" + p.getUnitPrice());
                }
                
                do {
                    // 3. Prompt for product ID
                    System.out.print("Enter the product ID you want to purchase: ");
                    String productId = scanner.nextLine();

                    // Validation loop
                    product = null; // Reset product
                    for (ProductVo p : productList) {
                        if (p.getId().equals(productId)) {
                            product = p;
                            break;
                        }
                    }
                    
                    if (product == null) {
                        System.out.println("Invalid product ID. Please try again.");
                    }
                

                    
                    // 4. Prompt for number of units
                    System.out.print("Enter the number of units: ");
                    int numberOfUnits = Integer.parseInt(scanner.nextLine());

                // Validation for units <= stock
                   if (numberOfUnits <= Integer.parseInt(product.getStock())) {

                       // 5. Create TransactionVo
                       TransactionVo transaction = new TransactionVo();
                       transaction.setBillerVo(billerVo);
                       transaction.setProductVo(product);
                       transaction.setNumberofUnits(String.valueOf(numberOfUnits));

                       // Calculate total amount for this product
                       double productTotalAmount = numberOfUnits * Double.parseDouble(product.getUnitPrice());
                       transaction.setTotalAmount(String.valueOf(productTotalAmount));

                       // Add to list of transactions
                       transactions.add(transaction);

                       // Update totalPurchaseAmount
                       double totalPurchaseAmount = 0.0;
                       for (TransactionVo t : transactions) {
                           totalPurchaseAmount += Double.parseDouble(t.getTotalAmount());
                       }
                       billerVo.setTotalPurchaseAmount(String.valueOf(totalPurchaseAmount));

                                LocalDateTime now = LocalDateTime.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                String formattedDate = now.format(formatter);
                                billerVo.setBillDate(formattedDate);
                   } else {
                        System.out.println("Not enough stock. Please select a different product or quantity.");
                    } 
                
                // 6. Continue/Exit Loop
                System.out.print("Do you want to purchase more products? (Y/N): ");
                
            } while (scanner.nextLine().equalsIgnoreCase("Y"));
                
//                transactions.forEach(x->System.out.println(x));
                
//				Invoke service layer method to perform some conversions to dto for this vo data
				String msg = billerService.createBill(transactions);
                break; 

             case 2:
                 // Show bill logic
                 List<TransactionVo> transactionVos = billerService.showBill();

                 // Display receipt
                 System.out.println("*** Receipt ***");
                 if (!transactionVos.isEmpty()) {
                     TransactionVo firstTransaction = transactionVos.get(0);
                     BillerVo billerVo1 = firstTransaction.getBillerVo();
                     System.out.println("Biller Name: " + billerVo1.getBillerName());
                     System.out.println("Biller Id: " + billerVo1.getBillerId());
                     System.out.println("Phone Number: " + billerVo1.getPhoneNumber());
                     System.out.println("Bill Date: " + billerVo1.getBillDate());
                     
                     System.out.println("\n--- Transactions ---");
                     System.out.println("Id\tProduct Name\tQuantity\tUnit Price\tTotal Amount");
                     System.out.println("-----------------------------------------------------------");
                     Double grandTotal = 0.0;
                     for (TransactionVo transaction : transactionVos) {
                         ProductVo productVo = transaction.getProductVo();
                         Double totalAmount =Double.parseDouble(transaction.getNumberofUnits())  * Double.parseDouble(productVo.getUnitPrice()) ;
                         grandTotal += totalAmount;
                         System.out.format("%s\t%s\t\t%s\t\t%s\t\t%.2f%n", transaction.getTransactionId(), productVo.getName(), transaction.getNumberofUnits(), productVo.getUnitPrice(), totalAmount);
                     }
                     System.out.println("-----------------------------------------------------------");
                     System.out.format("Total Purchase Amount: %.2f%n", grandTotal);
                 } else {
                     System.out.println("No transactions found.");
                 }
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
}