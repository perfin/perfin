package com.github.perfin.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.github.perfin.model.entity.Category;
import com.github.perfin.service.api.CategoryManager;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.PaginatedListWrapper;

@Stateless
@ApplicationPath("/resources")
@Path("categories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryManagerImpl implements CategoryManager {

	@PersistenceContext(unitName="primary")
	private EntityManager em;
	
	@Inject
	private UserManager userManager;
		
	public Category createCategory(Category category) {
		em.persist(category);
		
		return category;
	}

	public Category updateCategory(Category category) {
		
		Category updated = em.find(Category.class, category.getId());
		updated.setName(category.getName());
		updated.setUser(category.getUser());
		
		em.merge(category);
		
		return category;
	}

	@DELETE
    @Path("{id}")
    @Override
    public void deleteCategory(@PathParam("id") Long id) {
        Category category = em.find(Category.class, id);

        em.remove(category);
    }

	@Override
	public List<Category> getUserCategories() {
		
		Long userId = userManager.getCurrentUser().getId();
		
		Query query = em.createNamedQuery("getUserCategories");
		query.setParameter("userId", userId);
		
		List<Category> userCategories = query.getResultList();
		return userCategories;
	}
	
	@POST
	public Category saveCategory(Category category) {
		if(category.getId() == null) {
			return createCategory(category);
		} else {
			return updateCategory(category);
		}
	}
	
	@Override
	@GET
    public PaginatedListWrapper<Category> getCategories (
            @DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("id") @QueryParam("sortFields") String sortFields,
            @DefaultValue("asc") @QueryParam("sortDirections") String sortDirections) {
        PaginatedListWrapper<Category> paginatedListWrapper = new PaginatedListWrapper<>();
        paginatedListWrapper.setCurrentPage(page);
        paginatedListWrapper.setSortFields(sortFields);
        paginatedListWrapper.setSortDirections(sortDirections);
        paginatedListWrapper.setPageSize(5);
        
        return findCategories(paginatedListWrapper);
    }
	
	private PaginatedListWrapper<Category> findCategories(PaginatedListWrapper<Category> wrapper) {
		Long userId = userManager.getCurrentUser().getId();
        wrapper.setTotalResults(countCategories(userId));
        int start = (wrapper.getCurrentPage() - 1) * wrapper.getPageSize();
        wrapper.setList(findCategories(userId, start, wrapper.getPageSize(), wrapper.getSortFields(),
                wrapper.getSortDirections()));
        return wrapper;
    }
	
	private List<Category> findCategories(Long userId, int startPosition, int maxResults, String sortFields, String sortDirections) {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.user.id = " + userId+ " ORDER BY " + sortFields + " " + sortDirections);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }
	
	private Integer countCategories(Long userId) {
        Query query = em.createQuery("SELECT COUNT(c.id) FROM Category c WHERE c.user.id = " + userId);
        return ((Long) query.getSingleResult()).intValue();
    }

}
