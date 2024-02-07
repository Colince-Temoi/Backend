package tech.csm.service;

import java.util.List;

import tech.csm.entity.Sale;

public interface SaleService {

	Sale saveSale(Sale sale);

	List<Sale> getAllSales();

	Sale deleteSale(Integer sid);

	Sale getSaletoUpdate(Integer sid);

}
