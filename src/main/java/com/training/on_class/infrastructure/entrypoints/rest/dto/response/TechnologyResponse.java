package com.training.on_class.infrastructure.entrypoints.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TechnologyResponse {
    private Long id;
    private String name;
    private String description;
}
