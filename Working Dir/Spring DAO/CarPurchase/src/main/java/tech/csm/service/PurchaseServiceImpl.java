package tech.csm.service;

import java.util.List;

import tech.csm.dao.PurchaseDao;
import tech.csm.dao.PurchaseDaoImpl;
import tech.csm.entity.Purchase;

public class PurchaseServiceImpl implements PurchaseService {
 private static PurchaseDao purchaseDao=new PurchaseDaoImpl();
	
	@Override
	public String createPurchase(Purchase purchase) {
		return purchaseDao.createPurchase(purchase);
	}

	@Override
	public List<Purchase> getAllPurchases() {
		return purchaseDao.getAllPurchases();
	}

}
