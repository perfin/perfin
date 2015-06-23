package com.github.perfin.service.jms;

import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven
public class ConverterJmsProvider implements MessageListener {

    @Override
    public void onMessage(Message message) {

    }
}
