package com.github.perfin.service.jms;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.dto.ConvertJmsRequest;
import com.github.perfin.service.dto.ConvertJmsResponse;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSClient {
    private static final Logger log = Logger.getLogger(JMSClient.class.getName());
    // Set up all the default values
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/ConverterQueue";
    private static final String DEFAULT_DESTINATION_RESPONSE = "jms/ConverterQueueResponse";
    private static final String DEFAULT_USERNAME = "JMSUser";
    private static final String DEFAULT_PASSWORD = "User1234.";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";

    public static void main(String[] args) {
        Context namingContext = null;
        try {
            String userName = System.getProperty("username", DEFAULT_USERNAME);
            String password = System.getProperty("password", DEFAULT_PASSWORD);
            // Set up the namingContext for the JNDI lookup
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
            env.put(Context.SECURITY_PRINCIPAL, userName);
            env.put(Context.SECURITY_CREDENTIALS, password);
            namingContext = new InitialContext(env);
            // Perform the JNDI lookups
            String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
            log.info("Attempting to acquire connection factory \"" + connectionFactoryString + "\"");
            ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup(connectionFactoryString);
            log.info("Found connection factory \"" + connectionFactoryString + "\" in JNDI");
            String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
            log.info("Attempting to acquire destination \"" + destinationString + "\"");
            Destination destination = (Destination) namingContext.lookup(destinationString);
            Destination responseDestination = (Destination) namingContext.lookup(DEFAULT_DESTINATION_RESPONSE);
            log.info("Found destination \"" + destinationString + "\" in JNDI");

            Currency original = new Currency();
            original.setCode("EUR");

            Currency target = new Currency();
            target.setCode("USD");

            BigDecimal amountToConvert = new BigDecimal(1000);

            ConvertJmsRequest request = new ConvertJmsRequest(amountToConvert, original, target);

            try (JMSContext context = connectionFactory.createContext(userName, password)) {
                log.info("Sending message");

                Destination replyQueue = context.createTemporaryQueue();
                JMSConsumer consumer = context.createConsumer(responseDestination);

                ObjectMessage message = context.createObjectMessage();
                message.setObject(request);
                message.setJMSCorrelationID(UUID.randomUUID().toString());

                JMSProducer producer = context.createProducer();
                producer.send(destination, message);

                ObjectMessage reply = (ObjectMessage) consumer.receive(5000);

                ConvertJmsResponse response = (ConvertJmsResponse) reply.getObject();

                System.out.println("Amount: " + response.getAmount());
                System.out.println("Original currency: " + response.getOriginalCurrency().getCode());
                System.out.println("Target currency: " + response.getTargetCurrency().getCode());
                System.out.println("MessageID: " + message.getJMSMessageID());
                System.out.println("CorellID: " + message.getJMSCorrelationID());

            } catch (JMSException e) {
                e.printStackTrace();
            }
        } catch (NamingException e) {
            log.severe(e.getMessage());
        } finally {
            if (namingContext != null) {
                try {
                    namingContext.close();
                } catch (NamingException e) {
                    log.severe(e.getMessage());
                }
            }
        }
    }
}
