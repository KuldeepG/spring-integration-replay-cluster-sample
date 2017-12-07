package com.github.kuldeepg.cluster.configuration;

import com.github.kuldeepg.cluster.core.ClusterManager;
import com.github.kuldeepg.cluster.core.EvictionManager;
import com.github.kuldeepg.cluster.core.NamingStrategy;
import com.github.kuldeepg.cluster.core.StateManager;
import com.github.kuldeepg.cluster.repository.ClusterStateRepository;
import com.github.kuldeepg.cluster.repository.ListenerStateRepository;
import com.github.kuldeepg.query.channel.TransactionalChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.util.List;

@Configuration
@EnableScheduling
public class ClusterConfiguration {
  @Bean
  public StateManager stateManager(ClusterStateRepository clusterStateRepository,
                                   ListenerStateRepository listenerStateRepository,
                                   NamingStrategy namingStrategy,
                                   List<TransactionalChannel> messageChannels) {

    return new StateManager(clusterStateRepository,
        listenerStateRepository,
        namingStrategy,
        messageChannels);

  }

  @Bean
  public EvictionManager evictionManager(ListenerStateRepository listenerStateRepository,
                                         NamingStrategy namingStrategy,
                                         List<TransactionalChannel> messageChannels) {

    return new EvictionManager(listenerStateRepository, namingStrategy, messageChannels);
  }

  @Bean
  public ClusterManager clusterManager(StateManager stateManager,
                                       EvictionManager evictionManager,
                                       DefaultMessageListenerContainer listenerContainer) {

    return new ClusterManager(stateManager, evictionManager, listenerContainer);
  }

  @Bean
  public NamingStrategy namingStrategy() {
    String processName = ManagementFactory.getRuntimeMXBean().getName();
    return new NamingStrategy(processName);
  }
}
