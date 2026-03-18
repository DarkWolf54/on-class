package com.training.on_class.domain.ports.outbound;

import com.training.on_class.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyPersistencePort {
    Mono<Technology> saveTechnology(Technology technology);
    Mono<Boolean> existsByName(String name);
    Mono<Boolean> existAll(List<Long> ids);
    Flux<Technology> findAllByIds(List<Long> ids);
}
