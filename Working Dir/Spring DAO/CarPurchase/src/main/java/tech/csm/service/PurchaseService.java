package tech.csm.service;

import java.util.List;

import tech.csm.entity.Purchase;

public interface PurchaseService {

	String createPurchase(Purchase purchase);

	List<Purchase> getAllPurchases();

}
