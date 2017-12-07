package com.github.kuldeepg.query.channel;

import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.integration.dispatcher.BroadcastingDispatcher;
import org.springframework.integration.dispatcher.MessageDispatcher;
import org.springframework.messaging.Message;
import org.springframework.transaction.support.TransactionTemplate;

public class TransactionalChannel extends AbstractSubscribableChannel {
  private final MessageDispatcher dispatcher = new BroadcastingDispatcher();
  private final TransactionTemplate transactionTemplate;

  public TransactionalChannel(TransactionTemplate transactionTemplate) {
    this.transactionTemplate = transactionTemplate;
  }

  @Override
  protected boolean doSend(final Message<?> message, long timeout) {
    return transactionTemplate.execute(status -> getDispatcher().dispatch(message));
  }


  @Override
  protected MessageDispatcher getDispatcher() {
    return this.dispatcher;
  }
}
