package com.github.kuldeepg.cluster.core;

import com.github.kuldeepg.cluster.domain.ListenerState;
import com.github.kuldeepg.cluster.repository.ListenerStateRepository;
import com.github.kuldeepg.query.channel.TransactionalChannel;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class EvictionManager {
  private final ListenerStateRepository listenerStateRepository;
  private final NamingStrategy namingStrategy;
  private List<TransactionalChannel> messageChannels;

  public EvictionManager(ListenerStateRepository listenerStateRepository,
                         NamingStrategy clusterNamingStrategy,
                         List<TransactionalChannel> messageChannels) {
    this.listenerStateRepository = listenerStateRepository;
    this.namingStrategy = clusterNamingStrategy;
    this.messageChannels = messageChannels;
  }

  @Transactional
  public void process() {
    List<String> clusterNames = messageChannels.stream().map(namingStrategy::clusterName).collect(toList());
    for(String clusterName : clusterNames) {
      List<ListenerState> listenerStates = listenerStateRepository.findAllForCluster(clusterName);
      listenerStates.stream().filter(this::filter).forEach(listenerStateRepository::delete);
    }
  }

  private boolean filter(ListenerState listenerState) {
    Instant evictionMargin = Instant.now().minusSeconds(60);
    return listenerState.getCheckTime().toInstant().isBefore(evictionMargin);
  }
}
