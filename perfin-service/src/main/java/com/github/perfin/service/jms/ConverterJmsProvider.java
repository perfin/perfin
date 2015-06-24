package com.github.perfin.service.jms;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.api.CurrencyConverter;
import com.github.perfin.service.api.CurrencyManager;
import com.github.perfin.service.dto.ConvertJmsRequest;
import com.github.perfin.service.dto.ConvertJmsResponse;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.*;
import java.math.BigDecimal;
import java.util.List;

@MessageDriven(activationConfig = {
@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/ConverterQueue")
})
@RunAs("admin")
public class ConverterJmsProvider implements MessageListener {

    @Resource(name = "ConnectionFactory")
    private ConnectionFactory factory;

    @Resource(name = "java:/jms/ConverterQueueResponse")
    private Queue responseQueue;

    @Inject
    private CurrencyConverter currencyConverter;

    @Inject
    private CurrencyManager currencyManager;

    @Override
    @PermitAll
    public void onMessage(Message message) {
        try {
            ObjectMessage objMessage = (ObjectMessage) message;
            ConvertJmsRequest request = (ConvertJmsRequest) objMessage.getObject();
            Connection connection = factory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(responseQueue);
            connection.start();

            List<Currency> currencies = currencyManager.getCurrencies();

            Currency original = null;
            Currency target = null;

            for (Currency cur : currencies) {
                if (cur.getCode().equals(request.getOriginalCurrency().getCode())) {
                    original = cur;
                }
                if (cur.getCode().equals(request.getTargetCurrency().getCode())) {
                    target = cur;
                }
            }

            BigDecimal result = currencyConverter.convert(request.getAmount(), original, target);

            ConvertJmsResponse convertJmsResponse = new ConvertJmsResponse(result, request.getOriginalCurrency(), request.getTargetCurrency());

            Message response = session.createObjectMessage(convertJmsResponse);
            response.setJMSCorrelationID(message.getJMSCorrelationID());
            producer.send(response);


        } catch (JMSException ex) {
            throw new RuntimeException("problem with JMS", ex);
        }
    }
}