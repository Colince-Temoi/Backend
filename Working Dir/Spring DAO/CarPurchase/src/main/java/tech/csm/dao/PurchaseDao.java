package tech.csm.dao;

import java.util.List;

import tech.csm.entity.Purchase;

public interface PurchaseDao {

	String createPurchase(Purchase purchase);

	List<Purchase> getAllPurchases();

}
