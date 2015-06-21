package com.github.perfin.service.impl;

import com.github.perfin.model.entity.Resource;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.ResourceManager;
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
@ApplicationPath("/resources")
@Path("resources")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class ResourceManagerImpl implements ResourceManager {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserManager userManager;

    private Resource createResource(Resource resource) {
        resource.setUser(userManager.getCurrentUser());

        em.persist(resource);

        return resource;
    }

    private Resource updateResource(Resource resource) {
        Resource updated = em.find(Resource.class, resource.getId());
        updated.setName(resource.getName());
        updated.setCurrency(resource.getCurrency());
        updated.setBalance(resource.getBalance());

        em.merge(resource);

        return resource;
    }

    @Override
    @DELETE
    @Path("{id}")
    public void deleteResource(@PathParam("id") Long id) {
        Resource resource = em.find(Resource.class, id);
        em.remove(resource);
    }

    @Override
    @POST
    public Resource saveResource(Resource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Can't be saved null instance");
        }

        if (resource.getCurrency() == null) {
            resource.setCurrency(userManager.getCurrentUser().getDefaultCurrency());
        }

        if (resource.getId() == null) {
            return createResource(resource);
        } else {
            return updateResource(resource);
        }
    }

    @GET
    @Path("{id}")
    public Resource getResource(@PathParam("id") Long id) {
        return em.find(Resource.class, id);
    }

    @Override
    @GET
    public PaginatedListWrapper<Resource> getUserResources(
            @DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("id") @QueryParam("sortFields") String sortFields,
            @DefaultValue("asc") @QueryParam("sortDirections") String sortDirections) {

        PaginatedListWrapper<Resource> paginatedListWrapper = new PaginatedListWrapper<>();
        paginatedListWrapper.setCurrentPage(page);
        paginatedListWrapper.setSortFields(sortFields);
        paginatedListWrapper.setSortDirections(sortDirections);
        paginatedListWrapper.setPageSize(5);
        return findResources(paginatedListWrapper);
    }

    private PaginatedListWrapper<Resource> findResources(PaginatedListWrapper<Resource> wrapper) {
        wrapper.setTotalResults(countResources());
        int start = (wrapper.getCurrentPage() - 1) * wrapper.getPageSize();
        wrapper.setList(findResources(start, wrapper.getPageSize(), wrapper.getSortFields(),
                wrapper.getSortDirections()));
        return wrapper;
    }

    private Integer countResources() {
        User user = userManager.getCurrentUser();
        Query query = em.createQuery("SELECT COUNT(r.id) FROM Resource r WHERE r.user.id = " + user.getId());
        return ((Long) query.getSingleResult()).intValue();
    }

    private List<Resource> findResources(int startPosition, int maxResults, String sortFields, String sortDirections) {
        User user = userManager.getCurrentUser();
        Query query = em.createQuery("SELECT r FROM Resource r WHERE r.user.id = " + user.getId() + " ORDER BY " + sortFields + " " + sortDirections);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    void setUserManager(UserManager userManager) {
        this.userManager = userManager;

    }

    void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public Resource getResourceById(Long id) {
        Resource res = em.find(Resource.class, id);

        if (res != null) {
            if (!res.getUser().equals(userManager.getCurrentUser())) {
                return null;
            }
        }
        return res;
    }
}
