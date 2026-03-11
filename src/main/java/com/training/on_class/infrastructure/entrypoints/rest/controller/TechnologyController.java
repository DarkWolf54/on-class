package com.training.on_class.infrastructure.entrypoints.rest.controller;

import com.training.on_class.domain.model.Technology;
import com.training.on_class.domain.ports.inbound.ITechnologyServicePort;
import com.training.on_class.infrastructure.entrypoints.rest.dto.request.TechnologyRequest;
import com.training.on_class.infrastructure.entrypoints.rest.dto.response.SuccessResponse;
import com.training.on_class.infrastructure.entrypoints.rest.dto.response.TechnologyResponse;
import com.training.on_class.infrastructure.entrypoints.rest.mapper.ITechnologyRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/technologies")
@RequiredArgsConstructor
public class TechnologyController {

    private final ITechnologyServicePort technologyServicePort;
    private final ITechnologyRestMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SuccessResponse<TechnologyResponse>> createTechnology(@Valid @RequestBody TechnologyRequest request) {

        Technology technology = mapper.toDomain(request);

        return technologyServicePort.saveTechnology(technology)
          .map(mapper::toResponse)
          .map(dto -> new SuccessResponse<>("Tecnología creada exitosamente", dto));
    }
}