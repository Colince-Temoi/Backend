package tech.csm.productcrud.controller;

import java.util.List;
import java.util.Scanner;

import tech.csm.productcrud.domain.ProductVo;
import tech.csm.productcrud.service.ProductService;

public class ProductController {

    private ProductService productService; // Assuming you have a ProductService class

    public ProductController() {
        productService = new ProductServiceImpl(); // Initialize the service
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nProduct CRUD Menu:");
            System.out.println("1. Add Product");
            System.out.println("2. Show All Products");
            System.out.println("3. Search Product by Id");
            System.out.println("4. Update Product by Id");
            System.out.println("5. Delete Product by Id");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    ProductVo productVo = collectProductDetails(); 
                    String msg = productService.addProduct(productVo);
                    System.out.println(msg);
                    break;
                case 2:
                    List<ProductVo> pvoList= productService.showAllProducts();
                 // Display the product details from the pvoList. For example
                    for(ProductVo pvo : pvoList) {
                        System.out.println(pvo); // Assuming you have a toString() method in ProductVo
                    }
                    break;
                case 3:
                	System.out.print("Enter product ID to search: ");
                    int searchId = scanner.nextInt();
                    ProductVo pvo = productService.searchProductById(searchId);
                    // Display product details from pvo, if found
                    System.out.println(pvo);
                    break;
                case 4: 
                    List<ProductVo> allProducts = productService.showAllProducts(); 
                    // Display products for user to choose from, you can implement your own display logic here
                    for(ProductVo p : allProducts) {
                        System.out.println(p); 
                    }
                    System.out.print("Enter product ID to update: "); 
                    int updateId = scanner.nextInt(); 
                    ProductVo updatedProductVo = collectProductDetails(); // Reuse the method to collect product details 
                    Boolean status = productService.updateProductById(updateId, updatedProductVo); 
                    break; 

                case 5: 
                    List<ProductVo> allProducts1 = productService.showAllProducts(); 
                    // Display products for user to choose from, you can implement your own display logic here
                    for(ProductVo p : allProducts1) {
                        System.out.println(p); 
                    }
                    System.out.print("Enter product ID to delete: "); 
                    int deleteId = scanner.nextInt(); 
                    Boolean status1 = productService.deleteProductById(deleteId); 
                    break; 
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 0);
    }

    public static void main(String[] args) {
        ProductController controller = new ProductController();
        controller.start();
    }
    
    private ProductVo collectProductDetails() {
        Scanner scanner = new Scanner(System.in);
        ProductVo productVo = new ProductVo();

        System.out.print("Enter Product ID: ");
        productVo.setProductId(scanner.nextLine()); 

        System.out.print("Enter Product Name: ");
        productVo.setProductName(scanner.nextLine()); 

        System.out.print("Enter Unit Price: ");
        productVo.setUnitPrice(scanner.nextLine()); 

        System.out.print("Enter Manufacturing Date (YYYY-MM-DD): ");
        productVo.setManufacturingDate(scanner.nextLine()); 

        return productVo;
    }
    private void showProductsForSelection() {
        List<ProductVo> pvoList = productService.showAllProducts();
        if (pvoList.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        for (int i = 0; i < pvoList.size(); i++) {
            System.out.println((i  + 1) + ". " + pvoList.get(i)); 
        }
    }
}