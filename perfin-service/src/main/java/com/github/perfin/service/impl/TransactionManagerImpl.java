package com.github.perfin.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.github.perfin.model.entity.Category;
import com.github.perfin.model.entity.Resource;
import com.github.perfin.model.entity.Transaction;
import com.github.perfin.service.api.ResourceManager;
import com.github.perfin.service.api.TransactionManager;

@Stateless
public class TransactionManagerImpl implements TransactionManager {
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private ResourceManager resourceManager;

	/**
	 * TODO
	 */
	@Override
	public Transaction createTransaction(Resource resource, Category category,
			BigDecimal amount, String note) {
		// TODO
		return null;
	}
	
	/**
	 * TODO
	 */
	@Override
	public Transaction updateTransaction(Long id, Resource resource,
			Category category, BigDecimal amount, String note) {
		// TODO
		return null;
	}

	@Override
	public void deleteTransaction(Long id) {
		// TODO
	}

	@Override
	public List<Transaction> getTransactionsByDateRange(LocalDate startDate,
			LocalDate endDate) {
		Query query = em.createNamedQuery("getTransactionsByDateRange");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		
		List<Transaction> transactions =  query.getResultList();
		
		return transactions;
	}

	@Override
	public List<Transaction> getTransactionsByCategory(Category category) {
		Query query = em.createNamedQuery("getTransactionsByDateRange");
		query.setParameter("category", category);
		
		List<Transaction> transactions =  query.getResultList();
		
		return transactions;
	}

	@Override
	public List<Transaction> getTransactionsByResource(Resource resource) {
		Query query = em.createNamedQuery("getTransactionsByDateRange");
		query.setParameter("resource", resource);
		
		List<Transaction> transactions =  query.getResultList();
		
		return transactions;
	}

}
