package com.training.on_class.infrastructure.adapters.persistenceadapter.repositories;

import com.training.on_class.infrastructure.adapters.persistenceadapter.entities.TechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ITechnologyRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {
    Mono<Boolean> existsByName(String name);
}