package com.github.perfin.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.Resource;
import com.github.perfin.service.api.ResourceManager;
import com.github.perfin.service.api.UserManager;

@Stateless
public class ResourceManagerImpl implements ResourceManager {

	@PersistenceContext(unitName="primary")
	private EntityManager em;
	
	@Inject
	private UserManager userManager;
	
	@Override
	public Resource createResource(String name, BigDecimal initialBalance) {
		return createResource(name, initialBalance, userManager.getCurrentUser().getDefaultCurrency());
	}

	@Override
	public Resource createResource(String name, BigDecimal initialBalance,
			Currency currency) {
		Resource resource = new Resource();
		
		resource.setCurrency(currency);
		resource.setCurrentBalance(initialBalance);
		resource.setInitialBalance(initialBalance);
		resource.setName(name);
		resource.setUser(userManager.getCurrentUser());
		
		em.persist(resource);
		
		return resource;
	}

	@Override
	public Resource updateResource(Long id, String name) {
		Resource resource = em.find(Resource.class, id);
		resource.setName(name);
		
		em.merge(resource);
		
		return resource;
	}

	@Override
	public void deleteResource(Long id) {
		Resource resource = em.find(Resource.class, id);
		em.remove(resource);
	}

	@Override
	public List<Resource> getUserResources() {
		Query query = em.createNamedQuery("getUserResources");
		query.setParameter("userId", userManager.getCurrentUser().getId());
		
		List<Resource> userResources = query.getResultList();
		
		return userResources;
	}

}
