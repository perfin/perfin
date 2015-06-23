package com.github.perfin.service.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.github.perfin.model.entity.User;

/**
 * Only for test purpose
 * 
 * @author jomarko
 *
 */
@Stateless
public class UserManagerHelper {

    @PersistenceContext
    private EntityManager em;
    
    public User storeUser(User user) {
        em.persist(user);
        
        return user;
    }
}
