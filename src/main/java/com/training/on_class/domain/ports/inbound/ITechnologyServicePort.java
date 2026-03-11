package com.training.on_class.domain.ports.inbound;

import com.training.on_class.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface ITechnologyServicePort {
    Mono<Technology> saveTechnology(Technology technology);
}
