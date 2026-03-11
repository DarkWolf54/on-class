package com.training.on_class.infrastructure.entrypoints.rest.controller;

import com.training.on_class.domain.exceptions.BusinessException;
import com.training.on_class.domain.model.Technology;
import com.training.on_class.domain.ports.inbound.ITechnologyServicePort;
import com.training.on_class.infrastructure.entrypoints.rest.dto.request.TechnologyRequest;
import com.training.on_class.infrastructure.entrypoints.rest.dto.response.TechnologyResponse;
import com.training.on_class.infrastructure.entrypoints.rest.exception.GlobalExceptionHandler;
import com.training.on_class.infrastructure.entrypoints.rest.mapper.ITechnologyRestMapper;
import com.training.on_class.infrastructure.entrypoints.rest.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = TechnologyController.class)
@Import({GlobalExceptionHandler.class, SecurityConfig.class})
class TechnologyControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ITechnologyServicePort technologyServicePort;

    @MockitoBean
    private ITechnologyRestMapper technologyRestMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTechnology_Success() {
        // Arrange
        TechnologyRequest request = new TechnologyRequest();
        request.setName("Java");
        request.setDescription("Lenguaje backend");

        Technology mappedTechnology = new Technology(null, "Java", "Lenguaje backend");
        Technology savedTechnology = new Technology(1L, "Java", "Lenguaje backend");
        TechnologyResponse responseDto = new TechnologyResponse(1L, "Java", "Lenguaje backend");

        when(technologyRestMapper.toDomain(any(TechnologyRequest.class)))
          .thenReturn(mappedTechnology);

        when(technologyServicePort.saveTechnology(any(Technology.class)))
          .thenReturn(Mono.just(savedTechnology));

        when(technologyRestMapper.toResponse(any(Technology.class)))
          .thenReturn(responseDto);

        // Act & Assert
        webTestClient.post()
          .uri("/api/v1/technologies")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(request)
          .exchange()
          .expectStatus().isCreated()
          .expectBody()
          .jsonPath("$.message").isEqualTo("Tecnología creada exitosamente")
          .jsonPath("$.data.id").isEqualTo(1)
          .jsonPath("$.data.name").isEqualTo("Java")
          .jsonPath("$.data.description").isEqualTo("Lenguaje backend");

        verify(technologyRestMapper, times(1)).toDomain(any(TechnologyRequest.class));
        verify(technologyServicePort, times(1)).saveTechnology(any(Technology.class));
        verify(technologyRestMapper, times(1)).toResponse(any(Technology.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTechnology_Returns400_WhenNameIsBlank() {
        // Arrange
        TechnologyRequest request = new TechnologyRequest();
        request.setName("");
        request.setDescription("Lenguaje backend");

        // Act & Assert
        webTestClient.post()
          .uri("/api/v1/technologies")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(request)
          .exchange()
          .expectStatus().isBadRequest()
          .expectBody()
          .jsonPath("$.status").isEqualTo(400)
          .jsonPath("$.message").value(msg -> assertThat((String)msg).contains("El nombre es obligatorio"));

        verify(technologyRestMapper, never()).toDomain(any());
        verify(technologyServicePort, never()).saveTechnology(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTechnology_Returns400_WhenTechnologyAlreadyExists() {
        // Arrange
        TechnologyRequest request = new TechnologyRequest();
        request.setName("Java");
        request.setDescription("Lenguaje backend");

        Technology mappedTechnology = new Technology(null, "Java", "Lenguaje backend");

        when(technologyRestMapper.toDomain(any(TechnologyRequest.class)))
          .thenReturn(mappedTechnology);

        when(technologyServicePort.saveTechnology(any(Technology.class)))
          .thenReturn(Mono.error(new BusinessException("La tecnología ya existe")));

        // Act & Assert
        webTestClient.post()
          .uri("/api/v1/technologies")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(request)
          .exchange()
          .expectStatus().isBadRequest()
          .expectBody()
          .jsonPath("$.status").isEqualTo(400)
          .jsonPath("$.message").isEqualTo("La tecnología ya existe");

        verify(technologyRestMapper, times(1)).toDomain(any(TechnologyRequest.class));
        verify(technologyServicePort, times(1)).saveTechnology(any(Technology.class));
    }

    @Test
    @WithMockUser(roles = "PERSONA")
    void createTechnology_Returns403_WhenUserIsNotAdmin() {
        // Arrange
        TechnologyRequest request = new TechnologyRequest();
        request.setName("Java");
        request.setDescription("Lenguaje backend");

        // Act & Assert
        webTestClient.post()
          .uri("/api/v1/technologies")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(request)
          .exchange()
          .expectStatus().isForbidden();

        verify(technologyRestMapper, never()).toDomain(any());
        verify(technologyServicePort, never()).saveTechnology(any());
    }
}