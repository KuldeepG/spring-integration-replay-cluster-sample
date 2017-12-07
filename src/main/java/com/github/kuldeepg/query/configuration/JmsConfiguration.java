package com.github.kuldeepg.query.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;

import static com.github.kuldeepg.query.configuration.IntegrationFlowConfiguration.GREETINGS_QUEUE;

@Configuration
@Import(SerializerConfiguration.class)
public class JmsConfiguration {
  @Bean
  public JmsTemplate jmsTemplate(MessageConverter messageConverter, ConnectionFactory connectionFactory) {
    JmsTemplate jmsTemplate = new JmsTemplate();
    jmsTemplate.setConnectionFactory(connectionFactory);
    jmsTemplate.setMessageConverter(messageConverter);
    jmsTemplate.setDefaultDestinationName(GREETINGS_QUEUE);
    return jmsTemplate;
  }

}
