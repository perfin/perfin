package com.github.perfin.service.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.github.perfin.service.dto.ResourceBalance;
import com.google.gson.Gson;

public class ResourceBalanceEncoder implements Encoder.Text<ResourceBalance> {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String encode(ResourceBalance balance) throws EncodeException {
        if(balance == null) {
            return "{}";
        } else {
            Gson gson = new Gson();
            return gson.toJson(balance);
        }
        
    }

}
