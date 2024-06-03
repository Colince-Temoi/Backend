package tech.csm.productcrud.controller;

import java.util.Scanner;

import tech.csm.productcrud.dao.ProductDaoImpl;
import tech.csm.productcrud.domain.ProductDto;
import tech.csm.productcrud.domain.ProductVo;
import tech.csm.productcrud.service.ProductService;
import tech.csm.productcrud.service.ProductServiceImpl;

public class ProductController {
    private ProductService productService;
    private Scanner scanner;

    public ProductController(ProductService productService) {
        this.productService = productService;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("1. Add Product\n2. Show All Products\n3. Search Product by Id\n4. Update Product by Id\n5. Delete a product by id\n6. Sort By price ASC\n7. Sort By price DSC\n8. Sort by Manufacturing Date Dcs\n9. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // to consume newline left-over
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    showAllProducts();
                    break;

                case 3:
                    searchProductById();
                    break;
                case 4:
                    updateProductById();
                    break;

                case 5:
                    deleteProductById();
                    break;
                case 6:
                    // sortByPriceASC();
                    break;
                case 7:
                    // sortByPriceDSC();
                    break;
                case 8:
                    // sortByManufacturingDateDSC();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);
    }

    public void addProduct() {
        ProductVo productVo = new ProductVo();
        System.out.print("Enter product name: ");
        productVo.setProductName(scanner.nextLine());
        System.out.print("Enter product price: ");
        productVo.setPrice(scanner.nextLine());
        System.out.print("Enter product manufacturing date (yyyy-MM-dd): ");
        productVo.setManufacturingDate(scanner.nextLine());

        String savedProductId = productService.addProduct(productVo);
        System.out.println("Product saved successfully with id " + savedProductId);
    }
    
    public void showAllProducts() {
        productService.showAllProducts();
    }

    public void searchProductById() {
        System.out.print("Enter product id: ");
        String id = scanner.nextLine();
        ProductVo productDto = productService.searchProductById(id);
        if (productDto != null) {
            System.out.println("Product Name: " + productDto.getProductName());
            System.out.println("Product Price: " + productDto.getPrice());
            System.out.println("Product Manufacturing Date: " + productDto.getManufacturingDate());
        } else {
            System.out.println("Product not found with id " + id);
        }
    }

    public static void main(String[] args) {
        ProductService productService = new ProductServiceImpl(new ProductDaoImpl());
        ProductController mainController = new ProductController(productService);
        mainController.showMenu();
    }
    
    public void updateProductById() {
        System.out.print("Enter product id: ");
        String id = scanner.nextLine();
        ProductVo productVo = new ProductVo();
        System.out.print("Enter new product name: ");
        productVo.setProductName(scanner.nextLine());
        System.out.print("Enter new product price: ");
        productVo.setPrice(scanner.nextLine());
        System.out.print("Enter new product manufacturing date (yyyy-MM-dd): ");
        productVo.setManufacturingDate(scanner.nextLine());
        productService.updateProductById(id, productVo);
    }

    public void deleteProductById() {
        System.out.print("Enter product id: ");
        String id = scanner.nextLine();
        productService.deleteProductById(id);
    }
}