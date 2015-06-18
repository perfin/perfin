package com.github.perfin.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.github.perfin.model.entity.Category;
import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.Resource;
import com.github.perfin.model.entity.Transaction;
import com.github.perfin.service.api.ResourceManager;
import com.github.perfin.service.api.TransactionManager;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.PaginatedListWrapper;

@Stateless
@ApplicationPath("/resources")
@Path("transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionManagerImpl implements TransactionManager {
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private ResourceManager resourceManager;
	
	@Inject
	private UserManager userManager;

	public Transaction createTransaction(Transaction transaction) {
		em.persist(transaction);
		
		return transaction;
	}
	
	public Transaction updateTransaction(Transaction transaction) {
		throw new UnsupportedOperationException("unsupoorted yet");
	}

	@Override
	@DELETE
	@Path("{id}")
	public void deleteTransaction(@PathParam("id") Long id) {
		Transaction transaction = em.find(Transaction.class, id);
		
		Resource resource = transaction.getResource();
		BigDecimal currentBalance = resource.getCurrentBalance();
		resource.setCurrentBalance(currentBalance.subtract(transaction.getAmount()));
		
		resourceManager.saveResource(resource);
		
		em.remove(transaction);
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

	@Override
	@POST
	public Transaction saveTransaction(Transaction transaction) {
		if(transaction.getId() == null) {
			return createTransaction(transaction);
		} else {
			return updateTransaction(transaction);
		}
	}

	@Override
	public PaginatedListWrapper<Transaction> getTransactions(
			@DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("id") @QueryParam("sortFields") String sortFields,
            @DefaultValue("asc") @QueryParam("sortDirections") String sortDirections) {
		PaginatedListWrapper<Transaction> paginatedListWrapper = new PaginatedListWrapper<>();
        paginatedListWrapper.setCurrentPage(page);
        paginatedListWrapper.setSortFields(sortFields);
        paginatedListWrapper.setSortDirections(sortDirections);
        paginatedListWrapper.setPageSize(5);
        return findTransactions(paginatedListWrapper);
	}

	private PaginatedListWrapper<Transaction> findTransactions(
			PaginatedListWrapper<Transaction> wrapper) {
		wrapper.setTotalResults(countTransactions());
        int start = (wrapper.getCurrentPage() - 1) * wrapper.getPageSize();
        wrapper.setList(findTransactions(start, wrapper.getPageSize(), wrapper.getSortFields(),
                wrapper.getSortDirections()));
        return wrapper;
	}

	private List<Transaction> findTransactions(int startPosition, Integer maxResults,
			String sortFields, String sortDirections) {
		Long userId = userManager.getCurrentUser().getId();
		Query query = em.createQuery("SELECT t FROM Transaction t WHERE t.resource.user.id = " + userId + " ORDER BY " + sortFields + " " + sortDirections);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResults);
        return query.getResultList();
	}

	private Integer countTransactions() {
		Long userId = userManager.getCurrentUser().getId();
		Query query = em.createQuery("SELECT COUNT(t.id) FROM Transaction t WHERE t.resource.user.id = " + userId);
        return ((Long) query.getSingleResult()).intValue();
	}

}
