package com.github.perfin.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.ejb.Stateless;

import com.github.perfin.model.entity.Category;
import com.github.perfin.model.entity.Resource;
import com.github.perfin.model.entity.Transaction;
import com.github.perfin.service.api.TransactionManager;

@Stateless
public class TransactionManagerImpl implements TransactionManager {

	@Override
	public Transaction createTransaction(Resource resource, Category category,
			BigDecimal amount, String note) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction createTransfer(Resource originalResource,
			Resource targetResource, Category category,
			BigDecimal originalAmount, BigDecimal targetAmount, String note) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction updateTransaction(Long id, Resource resource,
			Category category, BigDecimal amount, String note) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTransaction(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Transaction> getTransactionsByDateRange(LocalDate startDate,
			LocalDate endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactionsByCategory(Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactionsByResource(Resource resource) {
		// TODO Auto-generated method stub
		return null;
	}

}
