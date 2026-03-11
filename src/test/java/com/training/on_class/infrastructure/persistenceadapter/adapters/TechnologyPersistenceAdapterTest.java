package com.training.on_class.infrastructure.persistenceadapter.adapters;

import com.training.on_class.domain.model.Technology;
import com.training.on_class.infrastructure.adapters.persistenceadapter.adapter.TechnologyPersistenceAdapter;
import com.training.on_class.infrastructure.adapters.persistenceadapter.entities.TechnologyEntity;
import com.training.on_class.infrastructure.adapters.persistenceadapter.mappers.ITechnologyEntityMapper;
import com.training.on_class.infrastructure.adapters.persistenceadapter.repositories.ITechnologyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyPersistenceAdapterTest {

    @Mock
    private ITechnologyRepository repository;

    @Mock
    private ITechnologyEntityMapper mapper;

    @InjectMocks
    private TechnologyPersistenceAdapter adapter;

    private Technology domainTechnology;
    private Technology savedDomainTechnology;
    private TechnologyEntity entity;
    private TechnologyEntity savedEntity;

    @BeforeEach
    void setUp() {
        domainTechnology = new Technology(null, "Java", "Backend language");

        entity = new TechnologyEntity(null, "Java", "Backend language");

        savedEntity = new TechnologyEntity(1L, "Java", "Backend language");

        savedDomainTechnology = new Technology(1L, "Java", "Backend language");
    }

    @Test
    void saveTechnology_Success() {
        // Arrange
        when(mapper.toEntity(domainTechnology)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(savedEntity));
        when(mapper.toDomain(savedEntity)).thenReturn(savedDomainTechnology);

        // Act
        Mono<Technology> result = adapter.saveTechnology(domainTechnology);

        // Assert
        StepVerifier.create(result)
          .expectNextMatches(tech -> tech.getId().equals(1L) && tech.getName().equals("Java"))
          .verifyComplete();

        verify(mapper, times(1)).toEntity(domainTechnology);
        verify(repository, times(1)).save(entity);
        verify(mapper, times(1)).toDomain(savedEntity);
    }

    @Test
    void existsByName_ReturnsTrue() {
        // Arrange
        String name = "Java";
        when(repository.existsByName(name)).thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = adapter.existsByName(name);

        // Assert
        StepVerifier.create(result)
          .expectNext(true)
          .verifyComplete();

        verify(repository, times(1)).existsByName(name);
    }

    @Test
    void existsByName_ReturnsFalse() {
        // Arrange
        String name = "Python";
        when(repository.existsByName(name)).thenReturn(Mono.just(false));

        // Act
        Mono<Boolean> result = adapter.existsByName(name);

        // Assert
        StepVerifier.create(result)
          .expectNext(false)
          .verifyComplete();

        verify(repository, times(1)).existsByName(name);
    }
}