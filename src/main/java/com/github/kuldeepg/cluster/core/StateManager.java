package com.github.kuldeepg.cluster.core;

import com.github.kuldeepg.cluster.domain.ClusterState;
import com.github.kuldeepg.cluster.domain.ListenerState;
import com.github.kuldeepg.cluster.domain.Mode;
import com.github.kuldeepg.cluster.domain.Slice;
import com.github.kuldeepg.cluster.repository.ClusterStateRepository;
import com.github.kuldeepg.cluster.repository.ListenerStateRepository;
import com.github.kuldeepg.query.channel.TransactionalChannel;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class StateManager {
  private final ClusterStateRepository clusterStateRepository;
  private final ListenerStateRepository listenerStateRepository;
  private final NamingStrategy namingStrategy;
  private List<TransactionalChannel> messageChannels;

  public StateManager(ClusterStateRepository clusterStateRepository,
                      ListenerStateRepository listenerStateRepository,
                      NamingStrategy clusterNamingStrategy,
                      List<TransactionalChannel> messageChannels) {
    this.clusterStateRepository = clusterStateRepository;
    this.listenerStateRepository = listenerStateRepository;
    this.namingStrategy = clusterNamingStrategy;
    this.messageChannels = messageChannels;
  }

  @PostConstruct
  @Transactional
  protected void initializeInitialState() {
    List<String> clusterNames = messageChannels.stream().
        map(namingStrategy::clusterName).
        collect(toList());

    for(String clusterName : clusterNames) {
      ClusterState clusterState = clusterStateRepository.findOne(clusterName);
      if(clusterState == null) {
        clusterState = new ClusterState(clusterName, Slice.SLICE_A, Mode.LIVE);
        clusterStateRepository.save(clusterState);
      }

      String listenerName = namingStrategy.listenerName(clusterName);
      ListenerState listenerState = new ListenerState(listenerName, clusterName, Mode.LIVE);

      listenerStateRepository.save(listenerState);
    }
  }

  public void process() {

    List<ClusterState> clusterStates = clusterStateRepository.findAllForSlice(Slice.SLICE_A);
    Map<ClusterState, List<ListenerState>> listenerStatesForClusters = clusterStates.
        stream().
        collect(toMap(Function.identity(), cs -> listenerStateRepository.findAllForCluster(cs.getId())));

    listenerStatesForClusters.forEach(this::updateListenerMode);

  }

  private void updateListenerMode(ClusterState clusterState, List<ListenerState> listenerStates) {
    Mode clusterMode = determineClusterMode(clusterState, listenerStates);
    String listenerId = namingStrategy.listenerName(clusterState.getId());
    Optional<ListenerState> currentListenerState = listenerStates.stream().
        filter(listenerState -> listenerState.getId().equals(listenerId)).findFirst();
    currentListenerState.ifPresent(listenerState -> {
      Mode listenerMode = determineListenerMode(clusterState, listenerState);

      updateClusterModeIfNecessary(clusterState, clusterMode);

      updateListenerState(listenerState, listenerMode);
    });

  }

  @Transactional
  private void updateListenerState(ListenerState currentListenerState, Mode listenerMode) {
    currentListenerState.setMode(listenerMode);
    currentListenerState.updateCheckTime();
    listenerStateRepository.save(currentListenerState);
  }

  @Transactional
  private void updateClusterModeIfNecessary(ClusterState clusterState, Mode clusterMode) {
    if(!clusterMode.equals(clusterState.getMode())) {
      clusterState.setMode(clusterMode);
      clusterStateRepository.save(clusterState);
    }
  }

  private Mode determineListenerMode(ClusterState clusterState, ListenerState listenerState) {

    if(clusterState.getMode().equals(Mode.LIVE))
      return Mode.LIVE;

    if(clusterState.getMode().equals(Mode.REPLAY) &&
        listenerState.getMode().equals(Mode.LIVE))
      return Mode.REPLAY;

    return listenerState.getMode();
  }

  private Mode determineClusterMode(ClusterState clusterState, List<ListenerState> listenerStates) {
    Mode currentClusterMode = clusterState.getMode();
    List<Mode> currentListenerModes = listenerStates.stream().map(ListenerState::getMode).collect(toList());

    if(currentClusterMode.equals(Mode.REPLAY)) {
      if (currentListenerModes.stream().allMatch(listenerMode -> listenerMode.equals(Mode.STOPPED)))
        return Mode.LIVE;
      else
        return Mode.REPLAY;
    }

    return Mode.LIVE;
  }

  @Transactional
  public void replayFor(String eventType) {
    String clusterId = eventType + ".channel";
    ClusterState clusterState = clusterStateRepository.findOne(clusterId);
    clusterState.setMode(Mode.REPLAY);
    ListenerState listenerState = listenerStateRepository.findOne(namingStrategy.listenerName(clusterId));
    listenerState.setMode(Mode.REPLAY);
    listenerState.updateCheckTime();

    clusterStateRepository.save(clusterState);
    listenerStateRepository.save(listenerState);
  }

  public void stop() {
    List<ClusterState> clusterStates = clusterStateRepository.findAllForSlice(Slice.SLICE_A);
    Map<ClusterState, List<ListenerState>> listenerStatesForClusters = clusterStates.
        stream().
        collect(toMap(Function.identity(), cs -> listenerStateRepository.findAllForCluster(cs.getId())));

    listenerStatesForClusters.forEach(this::stopListener);
  }

  private void stopListener(ClusterState clusterState, List<ListenerState> listenerStates) {
    String listenerId = namingStrategy.listenerName(clusterState.getId());
    Optional<ListenerState> currentListenerState = listenerStates.stream().
        filter(listenerState -> listenerState.getId().equals(listenerId)).findFirst();
    currentListenerState.ifPresent(listenerState -> {
      updateListenerState(listenerState, Mode.STOPPED);
    });
  }
}
