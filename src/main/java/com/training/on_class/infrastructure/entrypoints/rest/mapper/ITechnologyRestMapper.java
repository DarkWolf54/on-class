package com.training.on_class.infrastructure.entrypoints.rest.mapper;

import com.training.on_class.domain.model.Technology;
import com.training.on_class.infrastructure.entrypoints.rest.dto.request.TechnologyRequest;
import com.training.on_class.infrastructure.entrypoints.rest.dto.response.TechnologyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ITechnologyRestMapper {

    @Mapping(target = "id", ignore = true)
    Technology toDomain(TechnologyRequest request);

    TechnologyResponse toResponse(Technology technology);
}
