package com.github.perfin.service.websocket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.Resource;
import com.github.perfin.service.api.CurrencyConverter;
import com.github.perfin.service.api.ResourceManager;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.ResourceBalance;

@ServerEndpoint(value = "/balances", encoders = {ResourceBalanceEncoder.class})
public class ResourceBalanceEndpoint {
    
    @Inject
    private ResourceManager resourceManager;
    
    @Inject
    private CurrencyConverter currencyConverter;
    
    @Inject
    private UserManager userManager;
    
    @OnMessage
    public ResourceBalance getBalances(String message, Session peer) {
        if(message.equals("BALANCES")) {
            String userName = peer.getUserPrincipal().getName();
            Currency targetCurrency = userManager.getUser(userName).getDefaultCurrency();
            
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<Resource> resources = resourceManager.getUserResources(userName);
            Map<String, String> converted = new HashMap<String, String>();
            
            for(Resource res : resources) {
                BigDecimal convertedBalance = currencyConverter.convert(res.getBalance(), res.getCurrency(), targetCurrency);
                convertedBalance = convertedBalance.setScale(2, RoundingMode.CEILING);
                converted.put(res.getName(), String.valueOf(convertedBalance));
                totalAmount = totalAmount.add(convertedBalance);
            }
            return new ResourceBalance(converted, totalAmount.toString(), targetCurrency.getCode());
        } else {
            return null;
        }
        
    }
}
