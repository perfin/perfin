package com.github.perfin.service.impl;

import javax.ejb.Stateless;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.UserManager;


@Stateless
public class UserManagerImpl implements UserManager {

	@Override
	public void changeDefaultCurrency(Currency currency) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User getCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

}
