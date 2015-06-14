package com.github.perfin.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.github.perfin.model.entity.Category;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.CategoryManager;
import com.github.perfin.service.api.UserManager;

@Stateless
public class CategoryManagerImpl implements CategoryManager {

	@PersistenceContext(unitName="primary")
	private EntityManager em;
	
	
	@Inject
	private UserManager userManager; 
	
	@Override
	public Category createCategory(String name) {
		User loggedUser = userManager.getCurrentUser();
		
		Category category = new Category();
		category.setName(name);
		category.setUser(loggedUser);
		
		em.persist(category);
		
		return category;
	}

	@Override
	public Category updateCategory(Long id, String name) {
		
		Category category = em.find(Category.class, id);
		category.setName(name);
		
		em.merge(category);
		
		return category;
	}

	@Override
	public void deleteCategory(Long id) {
		Category category = em.find(Category.class, id);
		em.remove(category);		
	}

	@Override
	public List<Category> getUserCategories() {
		Query query = em.createNamedQuery("getUserCategories");
		query.setParameter("userId", userManager.getCurrentUser().getId());
		
		List<Category> userCategories = query.getResultList();
		return userCategories;
	}

}
