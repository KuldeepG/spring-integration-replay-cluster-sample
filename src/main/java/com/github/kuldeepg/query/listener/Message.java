package com.github.kuldeepg.query.listener;

import com.github.kuldeepg.query.annotation.EventHandler;
import com.github.kuldeepg.query.domain.Greeting;
import com.github.kuldeepg.query.model.MessageCreatedEvent;
import com.github.kuldeepg.query.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Message {
  public Message(GreetingRepository repository) {
    this.repository = repository;
  }

  @Autowired
  private GreetingRepository repository;

  @EventHandler
  public void writeToDb(MessageCreatedEvent messageCreatedEvent) {
    Greeting greeting = new Greeting(messageCreatedEvent.getMessage());
    repository.save(greeting);
    if(messageCreatedEvent.getMessage().equals("fail"))
      throw(new RuntimeException());
  }
}
