package com.github.kuldeepg.query.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.JmsMessageDrivenChannelAdapter;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;

@Configuration
@Import(JmsConfiguration.class)
public class IntegrationFlowConfiguration {

  public final static String GREETINGS_QUEUE = "greetings.queue";

  @Bean
  public IntegrationFlow flow(JmsMessageDrivenChannelAdapter jmsMessageDrivenChannelAdapter) {
    return IntegrationFlows
        .from(jmsMessageDrivenChannelAdapter)
        .route(message -> message.getClass().getName() + ".channel")
        .get();
  }

  @Bean
  public JmsMessageDrivenChannelAdapter jmsMessageDrivenChannelAdapter(
      AbstractMessageListenerContainer listenerContainer,
      ChannelPublishingJmsMessageListener listener) {

    JmsMessageDrivenChannelAdapter channelAdapter = new JmsMessageDrivenChannelAdapter(listenerContainer,
        listener);
    return channelAdapter;
  }

  @Bean
  public DefaultMessageListenerContainer listenerContainer(ConnectionFactory connectionFactory) {
    DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
    listenerContainer.setDestinationName(GREETINGS_QUEUE);
    listenerContainer.setConnectionFactory(connectionFactory);
    return listenerContainer;
  }

  @Bean
  public ChannelPublishingJmsMessageListener listener(MessageConverter messageConverter) {
    ChannelPublishingJmsMessageListener listener = new ChannelPublishingJmsMessageListener();
    listener.setMessageConverter(messageConverter);
    return listener;
  }

  @Bean
  public ChannelPostProcessor dynamicChannelConfiguration() {
    return new ChannelPostProcessor();
  }
}
