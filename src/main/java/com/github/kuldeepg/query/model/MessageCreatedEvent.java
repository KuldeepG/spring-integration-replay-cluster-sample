package com.github.kuldeepg.query.model;

import java.time.Instant;

public class MessageCreatedEvent {
  private String message;
  private Instant createdAt;

  @SuppressWarnings("unused")
  private MessageCreatedEvent() {
  }

  public MessageCreatedEvent(String message, Instant createdAt) {
    this.message = message;
    this.createdAt = createdAt;
  }

  public String getMessage() {
    return message;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
