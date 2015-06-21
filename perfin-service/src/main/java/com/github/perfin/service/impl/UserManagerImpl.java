package com.github.perfin.service.impl;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.UserManager;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.security.Principal;

@Stateless
@PermitAll
public class UserManagerImpl implements UserManager {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Principal principal;

    @Override
    public void changeDefaultCurrency(Currency currency) {
        User user = em.find(User.class, getCurrentUser().getId());
        user.setDefaultCurrency(currency);
        em.merge(user);
    }

    @Override
    public User getCurrentUser() {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.userName = :name", User.class);
        query.setParameter("name", principal.getName());
        return (User) query.getSingleResult();
    }

}
