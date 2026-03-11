package com.training.on_class.domain.model;

public class Technology {

    private Long id;
    private String name;
    private String description;

    public Technology(Long id, String name, String description) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio");
        if (name.length() > 50) throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        if (description == null || description.trim().isEmpty()) throw new IllegalArgumentException("La descripción es obligatoria");
        if (description.length() > 90) throw new IllegalArgumentException("La descripción no puede exceder los 90 caracteres");

        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
