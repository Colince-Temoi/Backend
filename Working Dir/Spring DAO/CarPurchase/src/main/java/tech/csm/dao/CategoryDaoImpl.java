package tech.csm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import tech.csm.entity.Category;
import tech.csm.util.DBUtil;

public class CategoryDaoImpl implements CategoryDao {

	@Override
	public List<Category> getAllCategories() {
		Session ses=DBUtil.getSessionFactory().openSession();
		Query<Category>qr=ses.createQuery("from Category");
		List<Category> categoryList =qr.list();
		return categoryList;
	}

}
