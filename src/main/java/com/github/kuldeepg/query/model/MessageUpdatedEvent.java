package com.github.kuldeepg.query.model;

import java.time.Instant;

public class MessageUpdatedEvent {
  private String message;
  private Instant updatedAt;

  @SuppressWarnings("unused")
  private MessageUpdatedEvent() {
  }

  public MessageUpdatedEvent(String message, Instant updatedAt) {
    this.message = message;
    this.updatedAt = updatedAt;
  }

  public String getMessage() {
    return message;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
