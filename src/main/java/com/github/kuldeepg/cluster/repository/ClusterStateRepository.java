package com.github.kuldeepg.cluster.repository;

import com.github.kuldeepg.cluster.domain.ClusterState;
import com.github.kuldeepg.cluster.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClusterStateRepository extends CrudRepository<ClusterState, String> {

  @Query("select state from ClusterState state where state.slice = ?1")
  List<ClusterState> findAllForSlice(Slice slice);
}
