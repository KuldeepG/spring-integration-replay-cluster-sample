package com.github.kuldeepg.cluster.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="listener_state")
public class ListenerState {

  @Id
  private String id;
  private String clusterId;
  private Mode mode;
  private Timestamp checkTime;

  @SuppressWarnings("unused")
  public ListenerState() {
  }

  public ListenerState(String id, String clusterId, Mode mode) {
    this.id = id;
    this.clusterId = clusterId;
    this.mode = mode;
    this.checkTime = Timestamp.valueOf(LocalDateTime.now());
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Mode getMode() {
    return mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

  public Timestamp getCheckTime() {
    return checkTime;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public void updateCheckTime() {
    this.checkTime = Timestamp.valueOf(LocalDateTime.now());
  }
}
