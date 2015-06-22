package com.github.perfin.service.impl;

import com.github.perfin.model.entity.Category;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.CategoryManager;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.PaginatedListWrapper;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@ApplicationPath("/service")
@Path("categories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class CategoryManagerImpl implements CategoryManager {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    @Inject
    private UserManager userManager;

    private Category createCategory(Category category) {
        em.persist(category);

        return category;
    }

    private Category updateCategory(Category category) {

        Category updated = em.find(Category.class, category.getId());
        updated.setName(category.getName());

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

    @POST
    public Category saveCategory(Category category) {
        if (category == null || category.getUser() == null) {
            throw new IllegalArgumentException(category + "can't be saved.");
        }
        if (category.getId() == null) {
            return createCategory(category);
        } else {
            return updateCategory(category);
        }
    }

    @GET
    @Path("{id}")
    @Override
    public Category getCategory(@PathParam("id") Long id) {
        Category category = em.find(Category.class, id);
        if(!category.getUser().equals(userManager.getCurrentUser())) {
            return null;
        }
        
        return category;
    }

    @Override
    @GET
    public PaginatedListWrapper<Category> getCategories(
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
        User user = userManager.getCurrentUser();
        Long userId = user.getId();
        wrapper.setTotalResults(countCategories(userId));
        int start = (wrapper.getCurrentPage() - 1) * wrapper.getPageSize();
        wrapper.setList(findCategories(userId, start, wrapper.getPageSize(), wrapper.getSortFields(),
                wrapper.getSortDirections()));
        return wrapper;
    }

    private List<Category> findCategories(Long userId, int startPosition, int maxResults, String sortFields, String sortDirections) {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.user.id = " + userId + " ORDER BY " + sortFields + " " + sortDirections);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    private Integer countCategories(Long userId) {
        Query query = em.createQuery("SELECT COUNT(c.id) FROM Category c WHERE c.user.id = " + userId);
        return ((Long) query.getSingleResult()).intValue();
    }

    void setUserManager(UserManager userManager) {
        this.userManager = userManager;

    }

    void setEntityManager(EntityManager em) {
        this.em = em;
    }

}
