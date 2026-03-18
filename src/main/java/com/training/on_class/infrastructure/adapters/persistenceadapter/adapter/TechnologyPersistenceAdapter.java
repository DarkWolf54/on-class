package com.training.on_class.infrastructure.adapters.persistenceadapter.adapter;

import com.training.on_class.domain.model.Technology;
import com.training.on_class.domain.ports.outbound.ITechnologyPersistencePort;
import com.training.on_class.infrastructure.adapters.persistenceadapter.entities.TechnologyEntity;
import com.training.on_class.infrastructure.adapters.persistenceadapter.mappers.ITechnologyEntityMapper;
import com.training.on_class.infrastructure.adapters.persistenceadapter.repositories.ITechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TechnologyPersistenceAdapter implements ITechnologyPersistencePort {

    private final ITechnologyRepository repository;
    private final ITechnologyEntityMapper mapper;

    @Override
    public Mono<Technology> saveTechnology(Technology technology) {
        TechnologyEntity entity = mapper.toEntity(technology);

        return repository.save(entity)
          .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Mono<Boolean> existAll(List<Long> ids) {
        long expectedSize = ids.stream().distinct().count();
        return repository.findAllById(ids)
          .count()
          .map(actualSize -> actualSize == expectedSize);
    }

    @Override
    public Flux<Technology> findAllByIds(List<Long> ids) {
        return repository.findAllById(ids)
          .map(entity -> new Technology(entity.getId(), entity.getName(), entity.getDescription()));
    }
}
