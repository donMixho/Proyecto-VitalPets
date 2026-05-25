package com.vitalpets.laboratorio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "examenes_laboratorio")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ExamenLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long mascotaId;

    @NotNull
    private Long clienteId;

    @NotNull
    private Long personalId;

    private Long citaId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoExamen tipoExamen;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoExamen estado = EstadoExamen.SOLICITADO;

    @Builder.Default
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    private LocalDateTime fechaResultado;

    @Column(length = 2000)
    private String resultados;

    @Column(length = 500)
    private String observaciones;

    private String archivoResultado;

    @Builder.Default
    private Boolean urgente = false;
}