package com.github.perfin.service.util;

public enum ExchangeCurrency {
	Czk("CZK"), Eur("EUR"), Usd("USD");
	
	private String label;
	
	private ExchangeCurrency(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
