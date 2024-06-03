package tech.csm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import tech.csm.entity.Purchase;
import tech.csm.util.DBUtil;

public class PurchaseDaoImpl implements PurchaseDao {


	@Override
	public String createPurchase(Purchase purchase) {
		Session ses =DBUtil.getSessionFactory().openSession();
		Transaction tx=ses.beginTransaction();
		ses.save(purchase);
		tx.commit();
		ses.close();
		return "Purchase successful!";
	}

	@Override
	public List<Purchase> getAllPurchases() {
		Session ses= DBUtil.getSessionFactory().openSession();
		Query<Purchase> sequery=ses.createQuery("from Purchase");
		List<Purchase>purchaselist= sequery.list();
		return purchaselist;
	}
	
	



	
}
