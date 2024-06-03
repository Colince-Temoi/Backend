package tech.csm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import tech.csm.entity.Product;
import tech.csm.util.DBUtil;

public class ProductDaoImpl implements ProductDao {

	@Override
	public List<Product> getProductByCategoryId(Integer cId) {
		Session ses=DBUtil.getSessionFactory().openSession();
		final String sequery="from Product p where p.category.cId=:cId";
		Query qr= ses.createQuery(sequery);
		qr.setParameter("cId", cId);
		List<Product> productList= qr.list();
		ses.close();
		return productList;
	}

}
