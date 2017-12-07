package com.github.kuldeepg.cluster.core;

import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.scheduling.annotation.Scheduled;

import javax.management.*;
import java.lang.management.ManagementFactory;

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

  @Scheduled(fixedDelay = 10000)
  public void logLastConsumedTime() throws MalformedObjectNameException, AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException {
    ObjectName objectName = ObjectName.getInstance("org.springframework.integration:type=MessageChannel,name=com.github.kuldeepg.query.model.MessageCreatedEvent.channel");

    Object lastSend = ManagementFactory.getPlatformMBeanServer()
        .getAttribute(objectName, "TimeSinceLastSend");

    System.out.println("===============");
    System.out.println(lastSend);
    System.out.println("===============");
  }

  public void reset() {
    stateManager.stop();
  }
}
