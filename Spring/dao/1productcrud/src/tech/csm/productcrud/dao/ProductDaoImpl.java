package tech.csm.productcrud.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tech.csm.productcrud.domain.Product;
/*Dao layer
 * 1. Used by service layer
 * 2. Interacts directly with the data store. This case our data store is AL.
 *    Operations
 *    -----------
 *    1. add == create||insert
 *    2. return the AL == Read all operation
 *    3. return a particular object in the AL == Read by id||name||...etc
 *    4. set == update operation.
 *    5. remove == delete operation.

 * Restricted to storing and retrieving data. Do not write any business logic here.
 * This has technology layer kind of things. i.e., Jdbc, Hibernate, JPA, Spring data Jpa
 * */
public class ProductDaoImpl implements ProductDao {

//	Data store -- Data is stored in heap area. Hence an AL store is an in-memory data store
	ArrayList<Product> productList;

//	0-arg constructor-We are using it to create our AL store object
	public ProductDaoImpl() {
		productList = new ArrayList<>();
	}

	@Override
	public String addProduct(Product product) {
		productList.add(product);
		return "1 product added!";
	}

	@Override
	public List<Product> displayAllProducts() {
		return productList;

	}

	@Override
	public Product getProductById(Integer id) {
//		Logic to get Product by Id from the AL data store
//		1. Iterate through the AL store
//		2. Maintain some flag to indicate when the product will be found
//		3. if product found store it into Product reference.
		Product product = null;
		Boolean found = false; // initializing my flag
		for (Product x : productList) {
//			If you used the == operator, then on the RHS of the operator, you must write the code as: id.intValue()
			if (x.getProductId().equals(id)) {
				product = x;
				found = true; // setting flag to true to indicate product is found
				break;
			}
		}
//		Like this, handle the !found case outside the loop.
		if (!found) {
			System.out.println("Product with id " + id + " does not exist. Try searching for another product!");
		}
		return product;

	}

	@Override
	public String updateProduct(Product product) {
//		find the product in store you want to update
		Product old = getProductById(product.getProductId());
		System.out.println(old.toString());
		int index = productList.indexOf(old);

		productList.set(index, product);
		return "Success";
	}

	/* Several things you need to do for the success of this operation.
	 * 1. Check if whatever you want to delete is existed or not. i.e., findById
	 * 2. Then you need to located where it is
	 * 3. Perform deletion on it.
	 * */
	@Override
	public String deleteProduct(Integer id) {

		Product product = getProductById(id);
//		System.out.println(productList.contains(product));

		int index = productList.indexOf(product);
		productList.remove(index);

		return "Successfully removed the product at index: " + index;

	}

	@Override
	public List<Product> sortByPriceAsc() {
		
		productList.sort(new Comparator<Product>() {

			@Override
			public int compare(Product o1, Product o2) {
				return (int) (o1.getUnitPrice()-o2.getUnitPrice());
			}
			
		});
		
		return productList;
		
//		System.out.println(productList);
		
	}
	@Override
	public List<Product> sortByPriceDsc() {
		
		productList.sort(new Comparator<Product>() {

			@Override
			public int compare(Product o1, Product o2) {
				return (int) -(o1.getUnitPrice()+o2.getUnitPrice());
			}
			
		});
		
		return productList;
		
//		System.out.println(productList); SORT BY MANUFACTURING DATE DESC
		
	}
	
	@Override
	public List<Product> sortByManufacturingDateDesc() {
		
		productList.sort(new Comparator<Product>() {

			@Override
			public int compare(Product o1, Product o2) {
				return (int) (o2.getManufacturingDate().getTime()-o1.getManufacturingDate().getTime());
			}
		});
		
		return productList;
		
	}
}
