package com.github.perfin.service.impl;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.UserManager;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Stateless
@ApplicationPath("/service")
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class UserManagerImpl implements UserManager {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Principal principal;

    @Resource
    private SessionContext sessionContext;

    @Override
    @POST
    public void changeDefaultCurrency(Currency currency) {
        User user = em.find(User.class, getCurrentUser().getId());
        user.setDefaultCurrency(currency);
        em.merge(user);
    }

    @Override
    @GET
    public User getCurrentUser() {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.userName = :name", User.class);
        query.setParameter("name", principal.getName());
        return (User) query.getSingleResult();
    }

    @Override
    public User getUser(String userName) {
        Query query = em.createNamedQuery("getUserByUserName");
        query.setParameter("userName", userName);
        return (User) query.getSingleResult();
    }

    @GET
    @Override
    public Map<String, Boolean> isInRole(@QueryParam("role") String role) {
        Map<String, Boolean> isInRole = new HashMap<>();
        isInRole.put(role, sessionContext.isCallerInRole(role));
        return isInRole;
    }

    void setEm(EntityManager em) {
        this.em = em;
    }

    void setPrincipal(Principal principal) {
        this.principal = principal;
    }
    
}
