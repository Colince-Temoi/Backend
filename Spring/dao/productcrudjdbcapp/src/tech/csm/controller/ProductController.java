package tech.csm.controller;

import java.util.List;
import java.util.Scanner;

import tech.csm.domain.ProductVo;
import tech.csm.service.ProductService;
import tech.csm.service.ProductServiceImpl;
import tech.csm.util.DbUtil;

public class ProductController {
	private static ProductVo pvo;
	private static Scanner scan;
//	private static Scanner ss;
	private static ProductService productService;

	public ProductController() {
		scan = new Scanner(System.in);
//		ss = new Scanner(System.in);
		pvo = new ProductVo();
		productService = new ProductServiceImpl();
	}

	public static void main(String[] args) {

		new ProductController();

		int choice;

		do {
			System.out.println("\n-----------Menu-------------" + "\n1.Add Product." + "\n2.Search Product by Id."
					+ "\n3.Show All Products." + "\n4.Update Product by Id." + "\n5.Delete a product by id"
					+ "\n6.Exit\n" + "\n Enter you choice: \n");

			choice = scan.nextInt();

			switch (choice) {
			case 1:
				addProductMenu();
				break;

			case 2: {
//				Take input from the user
				System.out.println("Enter product id which you want to search.");
				Integer id = scan.nextInt();

//				Invoke service layer method
				ProductVo pvo = productService.getProductById(id);
				if (pvo != null) {
					System.out.println("\n-----Searched product with the search id " + id + " -------\n");
					System.out.println(pvo.toString());
				} else {
					System.out.println("\nProduct with id " + id + " not found! try searching by another product id!");
				}

				break;
			}
			case 3: {

				List<ProductVo> list = productService.getAllProducts();

				if (list != null) {
					System.out.println("Below is the list of all products!-------");
					for (ProductVo productVo : list) {
						System.out.println(productVo.toString());
					}
				} else {
					System.out.println("Products not vailable! Try inserting first");
				}

				break;
			}
			case 4: {
				updateProductByIdMenu();
				break;
			}
			case 5: {
				deleteProductByIdMenu();
				break;
			}

			case 6:

				System.out.println("\nExiting the application. Goodbye!");
				break;

			default:
				System.out.println("\nInvalid choice. Please try again.");
				break;
			}
		} while (choice != 6);// Loop until the user chooses to exit
	}

//	Behavior to receive input
	public static ProductVo addProduct() {
		System.out.println("\n-------Insert Records into the t_product table-----\n");
		System.out.println("Enter Product name");
		pvo.setProductName(scan.next());
		System.out.println("Enter Product unit price");
		pvo.setUnitPrice(scan.next());
		System.out.println("Enter Product Manufacturing date[dd/mm/yyyy]");
		pvo.setMfgDate(scan.next());
		return pvo;
	}

	private static void addProductMenu() {
		char option;
		do {
			String msg = productService.addProduct(addProduct());
			System.out.println(msg);
			System.out.println("\nDo you want to insert another record?[y/n]");
			option = scan.next().charAt(0);
		} while (option == 'y');
	}

	private static void updateProductByIdMenu() {
		System.out.println("Enter product id which you want to update.");
		Integer idToUpdate = scan.nextInt();

		// Check if the product with the given id exists
		ProductVo existingProduct = productService.getProductById(idToUpdate);

		if (existingProduct != null) {
			// Product found, proceed with the update
			ProductVo updatedProduct = addProduct(); // Reuse the addProduct method for updating
			updatedProduct.setProductId(idToUpdate.toString()); // Set the id to the existing id

			String msg = productService.updateProduct(updatedProduct);
			System.out.println(msg);
		} else {
			System.out.println("\nProduct with id " + idToUpdate + " not found! Try updating with a valid product id!");
		}
	}

	private static void deleteProductByIdMenu() {
		System.out.println("Enter product id which you want to delete.");
		Integer idToDelete = scan.nextInt();

		// Check if the product with the given id exists
		ProductVo existingProduct = productService.getProductById(idToDelete);

		if (existingProduct != null) {
			// Product found, proceed with the deletion
			String msg = productService.deleteProductById(idToDelete);
			System.out.println(msg);
		} else {
			System.out.println("\nProduct with id " + idToDelete + " not found! Try deleting with a valid product id!");
		}
	}

// Testing that connection is established
	/*
	 * System.out.println(DbUtil.getConnection()); DbUtil.closeConnection();
	 * System.out.println(DbUtil.getConnection());
	 */

}
