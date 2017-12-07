package com.github.kuldeepg.query.repository;

import com.github.kuldeepg.query.domain.Greeting;
import org.springframework.data.repository.CrudRepository;

public interface GreetingRepository extends CrudRepository<Greeting, Long> {
}
