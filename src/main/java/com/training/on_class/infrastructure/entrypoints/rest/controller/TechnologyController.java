package com.training.on_class.infrastructure.entrypoints.rest.controller;

import com.training.on_class.domain.ports.inbound.ITechnologyServicePort;
import com.training.on_class.infrastructure.entrypoints.rest.dto.request.TechnologyRequest;
import com.training.on_class.infrastructure.entrypoints.rest.dto.response.ErrorResponse;
import com.training.on_class.infrastructure.entrypoints.rest.dto.response.SuccessResponse;
import com.training.on_class.infrastructure.entrypoints.rest.dto.response.TechnologyBasicResponse;
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

import java.util.List;

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
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),

      @ApiResponse(responseCode = "401", description = "Usuario no autenticado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),

      @ApiResponse(responseCode = "403", description = "El usuario no tiene el rol necesario (ADMIN)",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
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

    @Operation(summary = "Validar existencia de múltiples tecnologías",
      description = "Verifica si una lista de IDs de tecnologías existen en la base de datos. Usado para comunicación entre microservicios.")
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Validación ejecutada con éxito",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class)))
    })
    @GetMapping("/validate")
    public Mono<SuccessResponse<Boolean>> validateTechnologiesExist(@RequestParam List<Long> ids) {
        return technologyServicePort.existAll(ids)
          .map(allExist -> new SuccessResponse<>("Validación completada", allExist));
    }

    @Operation(summary = "Obtener tecnologías por IDs",
      description = "Devuelve una lista básica (solo ID y nombre) de las tecnologías solicitadas.")
    @GetMapping("/search")
    public Mono<SuccessResponse<List<TechnologyBasicResponse>>> getTechnologiesByIds(@RequestParam List<Long> ids) {

        return technologyServicePort.getAllByIds(ids)
          .map(tech -> new TechnologyBasicResponse(tech.getId(), tech.getName()))
          .collectList()
          .map(list -> new SuccessResponse<>("Tecnologías obtenidas exitosamente", list));
    }
}