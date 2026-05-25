package com.vitalpets.personal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "personal")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String rut;

    @NotBlank
    private String telefono;

    private String email;

    // Profesión formal (ej: "Médico Veterinario", "Técnico en Estética Animal")
    @NotBlank
    private String profesion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;

    // Implementos asignados (ej: "Guantes nitrilo, mascarilla, gafas protectoras")
    @Column(length = 500)
    private String implementosAsignados;

    @Builder.Default
    private Boolean activo = true;
}