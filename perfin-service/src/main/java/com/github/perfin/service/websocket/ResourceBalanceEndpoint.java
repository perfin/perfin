package com.github.perfin.service.websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.github.perfin.model.entity.Resource;
import com.github.perfin.service.api.ResourceManager;

@ServerEndpoint("/balances")
public class ResourceBalanceEndpoint {
    
    private static final Logger LOG = Logger.getLogger(ResourceBalanceEndpoint.class.getName());
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @Inject
    private ResourceManager resourceManager;
    
    /**
     * 
     * @param message format RESOURCE:{id}
     * @return message in format {resourceId: id, currentBalance:<balance>}
     *          {} - if no such resource exist or resource doesn't belong to user
     */
    @OnMessage
    public String hello(String message) {
        if(message.startsWith("RESOURCE:")) {
            String[] parts = message.split(":");
            if(parts.length != 2) {
                return "{}";
            }
            Long resId = Long.valueOf(parts[1]);
            Resource res = resourceManager.getResourceById(resId);
            if(res == null) {
                return "{}";
            }
            
            return "{resource: " + resId + ", currentBalance: " + res.getCurrentBalance() +"}";
        } else {
            return "{}";
        }
    }
 
    @OnOpen
    public void onOpen(Session peer) {
        LOG.info("WebSocket opened: " + peer.getId());
        peers.add(peer);
    }
 
    @OnClose
    public void onClose(Session peer) {
        LOG.info("Closing a WebSocket: " + peer.getId());
        peers.remove(peer);
    }
}
