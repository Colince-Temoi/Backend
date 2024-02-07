package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.entity.Product;
import tech.csm.entity.Sale;
import tech.csm.repository.SaleRepo;

@Service
public class SaleServiceImpl implements SaleService {
	
	@Autowired
	private SaleRepo saleRepo;
	
	@Autowired
	private ProductService productService;

	@Override
	public Sale saveSale(Sale sale) {
//		Test to see that the data is coming
//		System.out.println(sale);
		
//		Save the Sale: No need to perform any conversions from Vo to Dto here. This is taken care of behind the scenes.
		Product product = sale.getProduct();
		  
	    product.getProductStock();
//		Logic to save a sale
//		First of all update Product table by subtracting the number of units sold
		Product updatedProduct = productService.updateProductStockUnits(Integer.parseInt(sale.getNoOfUnits()),sale.getProduct().getProductId());
		
//		Now Save/Update the Sale
		Sale sale1 = saleRepo.save(sale);	
		
//		Test to verify that the saved sale is being returned to us
//		System.out.println(sale1);
		
		return sale1;
		
		
	}

	@Override
	public List<Sale> getAllSales() {
		List<Sale> salesList = saleRepo.findAll();
		return salesList;
	}

	@Override
	public Sale deleteSale(Integer sid) {
//		Logic to delete a Sale
//		Find the Sale to delete
		Sale sale = saleRepo.findById(sid).get();
//		Get product details for stock update
		Product product = sale.getProduct();
		Integer soldUnits = Integer.parseInt(sale.getNoOfUnits());
		
//		Update Product Stock
		productService.updateProductStockUnits(-soldUnits, product.getProductId());
		
//		Delete the Sale
		saleRepo.delete(sale);
		
		return sale;
	}

	@Override
	public Sale getSaletoUpdate(Integer sid) {	
		return saleRepo.findById(sid).get();
	}

}
