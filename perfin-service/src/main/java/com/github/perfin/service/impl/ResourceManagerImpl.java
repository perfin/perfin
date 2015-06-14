package com.github.perfin.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.Resource;
import com.github.perfin.service.api.ResourceManager;

@Stateless
public class ResourceManagerImpl implements ResourceManager {

	@Override
	public Resource createResource(String name, BigDecimal initialBalance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource createResource(String name, BigDecimal initialBalance,
			Currency currency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource updateResource(Long id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteResource(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Resource> getUserResources() {
		// TODO Auto-generated method stub
		return null;
	}

}
