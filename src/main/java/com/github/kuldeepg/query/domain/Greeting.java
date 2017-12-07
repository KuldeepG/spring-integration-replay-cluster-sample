package com.github.kuldeepg.query.domain;

import javax.persistence.*;

@Entity
@Table(name="greeting")
public class Greeting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String message;

  public Greeting() {
  }

  public Greeting(String message) {
    this.message = message;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "Greeting{" +
        "id=" + id +
        ", message='" + message + '\'' +
        '}';
  }
}
