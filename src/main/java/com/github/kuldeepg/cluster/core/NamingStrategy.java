package com.github.kuldeepg.cluster.core;

import com.github.kuldeepg.query.channel.TransactionalChannel;

public class NamingStrategy {

  private String processName;

  public NamingStrategy(String processName) {
    this.processName = processName;
  }

  public String clusterName(TransactionalChannel channel) {
    return channel.getFullChannelName();
  }

  public String listenerName(String clusterName) {
    return String.format("%s-%s", clusterName, processName);
  }
}
