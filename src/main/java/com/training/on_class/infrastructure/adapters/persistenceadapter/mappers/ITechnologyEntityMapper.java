package com.training.on_class.infrastructure.adapters.persistenceadapter.mappers;

import com.training.on_class.domain.model.Technology;
import com.training.on_class.infrastructure.adapters.persistenceadapter.entities.TechnologyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ITechnologyEntityMapper {

    TechnologyEntity toEntity(Technology technology);

    Technology toDomain(TechnologyEntity entity);
}
