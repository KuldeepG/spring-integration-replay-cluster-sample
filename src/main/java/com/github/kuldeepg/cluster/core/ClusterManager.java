package com.github.kuldeepg.cluster.core;

import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;

public class ClusterManager {

  private final StateManager stateManager;
  private final EvictionManager evictionManager;
  private final DefaultMessageListenerContainer listenerContainer;

  public ClusterManager(StateManager stateManager,
                        EvictionManager evictionManager,
                        DefaultMessageListenerContainer listenerContainer) {
    this.stateManager = stateManager;
    this.evictionManager = evictionManager;
    this.listenerContainer = listenerContainer;
  }

  @Scheduled(fixedDelay = 10000)
  public void run() {
    stateManager.process();
    evictionManager.process();
  }

  public void reset() {
    stateManager.stop();
  }
}
