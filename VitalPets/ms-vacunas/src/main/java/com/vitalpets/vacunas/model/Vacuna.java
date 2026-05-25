package com.vitalpets.vacunas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacunas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Vacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long mascotaId;

    @NotNull
    private Long clienteId;

    @NotNull
    private Long personalId;

    @NotBlank
    private String nombreVacuna;

    // Laboratorio fabricante
    private String laboratorio;

    // Número de lote del producto
    private String lote;

    @NotNull
    private LocalDate fechaAplicacion;

    // Cuándo vence la vacuna
    private LocalDate fechaProximaDosis;

    // Dosis aplicada (ej: "1ra dosis", "refuerzo")
    private String dosis;

    @Column(length = 500)
    private String observaciones;

    @Builder.Default
    private Boolean vigente = true;
}