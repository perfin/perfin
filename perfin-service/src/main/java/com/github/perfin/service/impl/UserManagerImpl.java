package com.github.perfin.service.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.UserManager;


@Stateless
public class UserManagerImpl implements UserManager {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public void changeDefaultCurrency(Currency currency) {
		User user = em.find(User.class, getCurrentUser().getId());
		user.setDefaultCurrency(currency);
		
		em.merge(user);
		
	}

	@Override
	public User getCurrentUser() {
		throw new UnsupportedOperationException("unspported yet");
	}

}
