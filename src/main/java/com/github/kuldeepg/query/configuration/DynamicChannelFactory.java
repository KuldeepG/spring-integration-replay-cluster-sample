package com.github.kuldeepg.query.configuration;

import com.github.kuldeepg.query.annotation.EventHandler;
import com.github.kuldeepg.query.channel.TransactionalChannel;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.integration.config.annotation.ServiceActivatorAnnotationPostProcessor;
import org.springframework.messaging.MessageHandler;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.Method;
import java.util.List;

import static java.util.Arrays.asList;

public class DynamicChannelFactory extends AbstractFactoryBean<TransactionalChannel> {
  private String channelName;
  private final List<Method> methods;

  public DynamicChannelFactory(String channelName, List<Method> methods) {
    this.channelName = channelName;
    this.methods = methods;
  }

  @Override
  public Class<?> getObjectType() {
    return TransactionalChannel.class;
  }

  @Override
  protected TransactionalChannel createInstance() throws Exception {
    ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) getBeanFactory();

    TransactionTemplate transactionTemplate = beanFactory.getBean(TransactionTemplate.class);
    TransactionalChannel channel = new TransactionalChannel(transactionTemplate);
    channel.setComponentName(channelName);

    for(Method method : methods) {
      Object listenerContainer = beanFactory.getBean(method.getDeclaringClass());

      EventHandler annotation = method.getAnnotation(EventHandler.class);
      MessageHandler handler = (MessageHandler) new ServiceActivatorAnnotationPostProcessor(beanFactory)
          .postProcess(listenerContainer,
              listenerContainer.getClass().getName(),
              method,
              asList(annotation));

      channel.subscribe(handler);
    }

    return channel;
  }
}
