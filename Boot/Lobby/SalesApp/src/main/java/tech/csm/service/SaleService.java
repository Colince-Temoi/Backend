package tech.csm.service;

import java.util.List;

import tech.csm.entity.Sale;

public interface SaleService {

	String saveSale(Sale sale);

	List<Sale> getAllSales();

	String deleteSale(Integer sid);

	Sale getSaletoUpdate(Integer sid);

}
