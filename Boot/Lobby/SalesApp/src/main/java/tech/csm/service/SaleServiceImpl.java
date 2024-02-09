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
	public String saveSale(Sale sale) {

		if (sale.getSalesId() == null) {
			Sale savedSale = saveOrUpdate(sale);
			return "Sale saved successfully!";
		} else {
			Sale updatedSale = saveOrUpdate(sale);
			return "Sale Updated successfully!";
		}
//		Test to verify that the saved sale is being returned to us
//		System.out.println(sale1);

	}

	private Sale saveOrUpdate(Sale sale) {
//		Logic to save a sale
//		First of all update Product table by subtracting the number of units sold
		Product product = sale.getProduct();
		product.getProductStock();
		Product updatedProduct = productService.updateProductStockUnits(Integer.parseInt(sale.getNoOfUnits()),
				sale.getProduct().getProductId());
//		Then Save/update the sale
		Sale sale1 = saleRepo.save(sale);	
		return sale1;
		
	}

	@Override
	public List<Sale> getAllSales() {
		List<Sale> salesList = saleRepo.findAll();
		return salesList;
	}

	@Override
	public String deleteSale(Integer sid) {
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

		return "Sale Deleted successfully!";
	}

	@Override
	public Sale getSaletoUpdate(Integer sid) {
		return saleRepo.findById(sid).get();
	}

}
