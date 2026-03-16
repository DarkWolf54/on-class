package com.training.on_class.infrastructure.entrypoints.rest.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Objeto que representa los datos de una tecnología registrada en el sistema")
public class TechnologyResponse {

    @Schema(description = "Identificador único autogenerado de la tecnología",
      example = "1")
    private Long id;

    @Schema(description = "Nombre de la tecnología",
      example = "Java")
    private String name;

    @Schema(description = "Descripción detallada de la tecnología",
      example = "Lenguaje de programación orientado a objetos para desarrollo backend.")
    private String description;
}