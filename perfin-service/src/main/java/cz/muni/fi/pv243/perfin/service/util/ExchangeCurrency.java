package cz.muni.fi.pv243.perfin.service.util;

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
