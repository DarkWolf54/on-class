package com.training.on_class.infrastructure.entrypoints.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Objeto que representa la petición para crear una nueva tecnología en el sistema")
public class TechnologyRequest {

    @Schema(description = "Nombre de la tecnología. Debe ser único en el sistema.",
      example = "Java",
      requiredMode = Schema.RequiredMode.REQUIRED,
      maxLength = 50)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String name;

    @Schema(description = "Descripción detallada de para qué sirve la tecnología.",
      example = "Lenguaje de programación orientado a objetos para desarrollo backend.",
      requiredMode = Schema.RequiredMode.REQUIRED,
      maxLength = 90)
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 90, message = "La descripción no puede exceder los 90 caracteres")
    private String description;
}