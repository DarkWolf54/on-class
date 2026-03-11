package com.training.on_class.domain.ports.outbound;

import com.training.on_class.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface ITechnologyPersistencePort {
    Mono<Technology> saveTechnology(Technology technology);
    Mono<Boolean> existsByName(String name);
}
