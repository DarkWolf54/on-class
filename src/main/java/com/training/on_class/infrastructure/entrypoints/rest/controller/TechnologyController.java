package com.training.on_class.infrastructure.entrypoints.rest.controller;

import com.training.on_class.domain.ports.inbound.ITechnologyServicePort;
import com.training.on_class.infrastructure.entrypoints.rest.dto.request.TechnologyRequest;
import com.training.on_class.infrastructure.entrypoints.rest.dto.response.SuccessResponse;
import com.training.on_class.infrastructure.entrypoints.rest.dto.response.TechnologyResponse;
import com.training.on_class.infrastructure.entrypoints.rest.mapper.ITechnologyRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/technologies")
@RequiredArgsConstructor
@Tag(name = "Technology", description = "Endpoints para la gestión de tecnologías del bootcamp")
public class TechnologyController {

    private final ITechnologyServicePort technologyServicePort;
    private final ITechnologyRestMapper mapper;

    @Operation(summary = "Crear una nueva tecnología",
      description = "Permite a un administrador registrar una tecnología validando que su nombre sea único.")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Tecnología creada exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class))),
      @ApiResponse(responseCode = "400", description = "Error de validación en los campos o nombre de tecnología duplicado",
        content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "401", description = "Usuario no autenticado",
        content = @Content),
      @ApiResponse(responseCode = "403", description = "El usuario no tiene el rol necesario (ADMIN)",
        content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SuccessResponse<TechnologyResponse>> createTechnology(
      @Valid @RequestBody Mono<TechnologyRequest> requestMono) {
        return requestMono
          .map(mapper::toDomain)
          .flatMap(technologyServicePort::saveTechnology)
          .map(mapper::toResponse)
          .map(dto -> new SuccessResponse<>("Tecnología creada exitosamente", dto));
    }
}