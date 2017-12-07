package com.github.kuldeepg.query.web;

import com.github.kuldeepg.query.model.MessageCreatedEvent;
import com.github.kuldeepg.query.model.MessageUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static java.time.Instant.now;

@RestController
public class GreetingsController {

  @Autowired
  private JmsTemplate jmsTemplate;

  @PostMapping("greeting")
  public ResponseEntity createGreeting(@RequestBody String message) {
    jmsTemplate.convertAndSend(new MessageCreatedEvent(message, now()));
    return ResponseEntity.created(URI.create("http://localhost:8080")).build();
  }

  @PutMapping("greeting")
  public ResponseEntity updateGreeting(@RequestBody String message) {
    jmsTemplate.convertAndSend(new MessageUpdatedEvent(message, now()));
    return ResponseEntity.created(URI.create("http://localhost:8080")).build();
  }
}
