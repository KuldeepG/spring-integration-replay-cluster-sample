package com.github.kuldeepg.cluster.repository;

import com.github.kuldeepg.cluster.domain.ClusterState;
import com.github.kuldeepg.cluster.domain.ListenerState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListenerStateRepository extends CrudRepository<ListenerState, String> {

  @Query("select state from ListenerState state where state.clusterId = ?1")
  List<ListenerState> findAllForCluster(String clusterId);
}
