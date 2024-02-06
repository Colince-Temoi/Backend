package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.entity.Sale;
import tech.csm.repository.SaleRepo;

@Service
public class SaleServiceImpl implements SaleService {
	
	@Autowired
	private SaleRepo saleRepo;

	@Override
	public Sale saveSale(Sale sale) {
//		Test to see that the data is coming
//		System.out.println(sale);
		
//		Save the Sale: No need to perform any conversions from Vo to Dto here. This is taken care of behind the scenes.
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
		
		return null;
	}

}
