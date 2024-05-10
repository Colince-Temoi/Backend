package tech.csm.productcrud.controller;

import java.util.Scanner;

import tech.csm.productcrud.domain.ProductVo;
import tech.csm.productcrud.service.ProductService;
import tech.csm.productcrud.service.ProductServiceImpl;

public class ProductController {

	// Secondary dependancy
	private static ProductService productService = new ProductServiceImpl();
	private static ProductVo pvo = new ProductVo();

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		int choice;

		do {
			System.out.println("\n-----------Menu-------------" + "\n1.Add Product." + "\n2.Show All Products."
					+ "\n3.Search Product by Id." + "\n4.Update Product by Id." + "\n5.Delete a product by id"+"\n6.Sort By price ASC."
					+ "\n7.Sort by Manufacturing Date Decs." + "\n8.Exit\n" + "\n Enter you choice: \n");

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

				System.out.println(msg);

				break;

			case 2: {
				System.out.println("\n--------All products---------\n");
				for (ProductVo x : productService.getAllProducts()) {
					System.out.println(x.toString());
				}

				break;
			}
			case 3: {
				System.out.println("\nEnter Product Id your want to search for\n");
				int id = sc.nextInt();
				System.out.println("\n---------Returned Product------\n");
				System.out.println(productService.getProductById(id).toString());

				break;
			}
			case 4: {
				System.out.println("\n---Update product details-------\n");
				System.out.println("\nEnter Id to product you want to update\n");
				int id = sc.nextInt();

				System.out.println("\n---------Old product details------\n");
				System.out.println(productService.getProductById(id).toString());

				System.out.println("Enter new Product name");
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
				
//				Product list after deletion of a product
				System.out.println("\n--------All products after deletion of some other products---------\n");
				for (ProductVo productList : productService.getAllProducts()) {
					System.out.println(productList.toString());
				}
				break;
			}
			default:
				break;
			}
		} while (choice <= 7);
	}
}
