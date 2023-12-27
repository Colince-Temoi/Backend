package tech.csm.productcrud.controller;

import java.util.Scanner;

import tech.csm.productcrud.domain.Product;
import tech.csm.productcrud.domain.ProductVo;
import tech.csm.productcrud.service.ProductService;
import tech.csm.productcrud.service.ProductServiceImpl;

// This type of architecture is recommeded as we can easily add or remove functionalities as and when needed.

/* In real time:
 * Data is coming in the form of String from UI.
 * This data you have to straight away store in Vo, this is how Vo helps us.
 * When we need this data in our required format, convert it into Dto.
 * In high level frameworks like Spring, no need to maintain any Vo. Automatically things will be converted into Dto
 * Controller purpose will be:
 * Invoking of service layer behaviors based on the incoming request type.
 * 
 * */

/*Controller responsibilities
 * 1. Get input from the Front-end application
 * 2. Invoke Service layer business methods.
 * 
 * Note: Do not write business logic in this layer.
 * */
public class ProductController {

	// Secondary dependancy
	private static ProductService productService = new ProductServiceImpl();
	private static ProductVo pvo = new ProductVo();

	
	// This is acting as our UI layer
	/*UI layer only knows Vo type
	 * 
	 * */
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		int choice;

		do {
			System.out.println("\n-----------Menu-------------" + "\n1.Add Product." + "\n2.Show All Products."
					+ "\n3.Search Product by Id." + "\n4.Update Product by Id." + "\n5.Delete a product by id"
					+ "\n6.Sort By price ASC."+ "\n7.Sort By price DSC." + "\n8.Sort by Manufacturing Date Dcs." + "\n9.Exit\n"
					+ "\n Enter you choice: \n");

			choice = sc.nextInt();

			switch (choice) {
			case 1:
				System.out.println("Enter Product Id");
				pvo.setProductId(sc.next());
				System.out.println("Enter Product name");
				pvo.setProductName(sc.next());
				System.out.println("Enter Product unit price");
				pvo.setUnitPrice(sc.next());
				System.out.println("Enter Product Manufacturing date[dd/mm/yyyy]");
				pvo.setManufacturingDate(sc.next());

				String msg = productService.addProduct(pvo);

				System.out.println("\n" + msg);

				break;

			case 2: {
				System.out.println("\n--------All products---------\n");
				for (ProductVo x : productService.getAllProducts()) {
					System.out.println(x.toString());
				}

				break;
			}
			case 3: {
				System.out.println("\nEnter Product Id your want to search for: ");
				int id = sc.nextInt();
				System.out.println("\n---------Returned Product------\n");
				System.out.println(productService.getProductById(id).toString());

				break;
			}
			case 4: {
				System.out.println("\n---Update product details-------\n");
				System.out.println("\nEnter Id to product you want to update\n");
				Integer id = sc.nextInt();

				System.out.println("\n---------Old product details------\n");
				System.out.println(productService.getProductById(id).toString());

				pvo.setProductId(id.toString());
				
				System.out.println("\nEnter new Product name");
				pvo.setProductName(sc.next());

				System.out.println("Enter new Product unit price");
				pvo.setUnitPrice(sc.next());

				System.out.println("Enter new Product Manufacturing date[dd/mm/yyyy]");
				pvo.setManufacturingDate(sc.next());

				String msg1 = productService.updateProduct(pvo);
				System.out.println(msg1);

				System.out.println("\n---------New updated product details------\n");
				System.out.println(productService.getProductById(id).toString());
				break;
			}
			case 5: {
//				Accept an id as input
				System.out.println("\nEnter Id to product you want to delete\n");
				int id = sc.nextInt();
				String msg2 = productService.deleteProduct(id);

				System.out.println(msg2);

//				This should return null because the product is deleted
//				System.out.println(productService.getProductById(id).toString());

//				Product list after deletion of a product
				System.out.println("\n--------All products after deletion of product with the id "+id+" ---------\n");
				for (ProductVo productList : productService.getAllProducts()) {
					System.out.println(productList.toString());
				}
				break;
			}

			case 6: {
				System.out.println("----------Sorted by price ASC order----------");
				for (Product x : productService.sortByPriceAsc()) {
					System.out.println(x);
				}

				break;
			}

			case 7: {
				System.out.println("----------Sorted by price in DSC order----------");
				for (Product x : productService.sortByPriceDsc()) {
					System.out.println(x);
				}

				break;
			}
			
			case 8: {
				System.out.println("----------Sorted by manufacturing date in DSC order----------");
				for (Product x : productService.sortByManufacturingDateDesc()) {
					System.out.println(x.toString());
				}

				break;
			}
			case 9: 
//				Like this we can exit the program execution
				System.out.println("\nExiting the application. Goodbye!");
				break;

			default:
				System.out.println("\nInvalid choice. Please try again.");
				break;
			}
		} while (choice != 9);// Loop until the user chooses to exit
	}
}
