package com.training.on_class.domain.usecase;

import com.training.on_class.domain.exceptions.BusinessException;
import com.training.on_class.domain.model.Technology;
import com.training.on_class.domain.ports.outbound.ITechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {

    @Mock
    private ITechnologyPersistencePort persistencePort;

    @InjectMocks
    private TechnologyUseCase technologyUseCase;

    private Technology newTechnology;
    private Technology savedTechnology;

    @BeforeEach
    void setUp() {
        newTechnology = new Technology(null, "Java", "Lenguaje de programación backend");
        savedTechnology = new Technology(1L, "Java", "Lenguaje de programación backend");
    }

    @Test
    void saveTechnology_Success() {
        // Arrange
        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(persistencePort.saveTechnology(any(Technology.class))).thenReturn(Mono.just(savedTechnology));

        // Act
        Mono<Technology> result = technologyUseCase.saveTechnology(newTechnology);

        // Assert
        StepVerifier.create(result)
          .expectNextMatches(tech -> tech.getId().equals(1L) && tech.getName().equals("Java"))
          .verifyComplete();

        verify(persistencePort, times(1)).existsByName("Java");
        verify(persistencePort, times(1)).saveTechnology(newTechnology);
    }

    @Test
    void saveTechnology_ThrowsException_WhenNameExists() {
        // Arrange
        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(true));

        // Act
        Mono<Technology> result = technologyUseCase.saveTechnology(newTechnology);

        // Assert
        StepVerifier.create(result)
          .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
            throwable.getMessage().equals("La tecnología ya existe"))
          .verify();

        verify(persistencePort, times(1)).existsByName("Java");
        verify(persistencePort, never()).saveTechnology(any(Technology.class));
    }
}