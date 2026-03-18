package com.training.on_class.domain.ports.inbound;

import com.training.on_class.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyServicePort {
    Mono<Technology> saveTechnology(Technology technology);
    Mono<Boolean> existAll(List<Long> ids);
    Flux<Technology> getAllByIds(List<Long> ids);
}
