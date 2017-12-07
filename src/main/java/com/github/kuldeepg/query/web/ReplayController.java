package com.github.kuldeepg.query.web;

import com.github.kuldeepg.cluster.core.StateManager;
import com.github.kuldeepg.cluster.domain.ClusterState;
import com.github.kuldeepg.cluster.domain.ListenerState;
import com.github.kuldeepg.cluster.repository.ClusterStateRepository;
import com.github.kuldeepg.cluster.repository.ListenerStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ReplayController {

  @Autowired
  private StateManager manager;

  @Autowired
  private ClusterStateRepository clusterStateRepository;

  @Autowired
  private ListenerStateRepository listenerStateRepository;

  @PostMapping("replay")
  public ResponseEntity createGreeting(@RequestBody String eventType) {
    manager.replayFor(eventType);
    return ResponseEntity.ok("Done");
  }

  @GetMapping("replay/status")
  public ResponseEntity status() {
    Iterable<ClusterState> clusterStates = clusterStateRepository.findAll();
    Iterable<ListenerState> listenerStates = listenerStateRepository.findAll();

    Map<Object, Object> result = new HashMap<>();
    result.put("clusters", clusterStates);
    result.put("listeners", listenerStates);

    return ResponseEntity.ok(result);
  }

  @PostMapping("replay/stop")
  public ResponseEntity stop() {
    manager.stop();

    return ResponseEntity.ok("Stopping!");
  }
}
