package tech.csm.service;

import java.util.List;

import tech.csm.dao.CategoryDao;
import tech.csm.dao.CategoryDaoImpl;
import tech.csm.entity.Category;

public class CategoryServiceImpl implements CategoryService {
private CategoryDao categoryDao= new CategoryDaoImpl();
	
	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return categoryDao.getAllCategories();
	}

}
