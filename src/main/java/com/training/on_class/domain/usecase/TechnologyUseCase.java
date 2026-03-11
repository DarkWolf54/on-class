package com.training.on_class.domain.usecase;

import com.training.on_class.domain.exceptions.BusinessException;
import com.training.on_class.domain.model.Technology;
import com.training.on_class.domain.ports.inbound.ITechnologyServicePort;
import com.training.on_class.domain.ports.outbound.ITechnologyPersistencePort;
import reactor.core.publisher.Mono;

public class TechnologyUseCase implements ITechnologyServicePort {

    private final ITechnologyPersistencePort persistencePort;

    public TechnologyUseCase(ITechnologyPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Technology> saveTechnology(Technology technology) {
        return persistencePort.existsByName(technology.getName())
          .flatMap(exists -> {
              if (Boolean.TRUE.equals(exists)) {
                  return Mono.error(new BusinessException("La tecnología ya existe"));
              }
              return persistencePort.saveTechnology(technology);
          });
    }
}
