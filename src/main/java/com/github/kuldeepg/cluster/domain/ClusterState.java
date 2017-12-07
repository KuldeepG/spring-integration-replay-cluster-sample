package com.github.kuldeepg.cluster.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cluster_state")
public class ClusterState {

  @Id
  private String id;
  private Slice slice;
  private Mode mode;

  @SuppressWarnings("unused")
  public ClusterState() {
  }

  public ClusterState(String id, Slice slice, Mode mode) {
    this.id = id;
    this.slice = slice;
    this.mode = mode;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Slice getSlice() {
    return slice;
  }

  public void setSlice(Slice slice) {
    this.slice = slice;
  }

  public Mode getMode() {
    return mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }
}
