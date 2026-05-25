package com.vitalpets.citas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID de la mascota (MS-Mascotas 8081)
    @NotNull
    @Column(nullable = false)
    private Long mascotaId;

    // ID del dueño legal (MS-Clientes 8082)
    @NotNull
    @Column(nullable = false)
    private Long clienteId;

    // ID del tercero autorizado (puede ser null si lo trae el dueño)
    private Long terceroId;

    // ID del personal asignado (MS-Personal 8087)
    @NotNull
    @Column(nullable = false)
    private Long personalId;

    @NotNull
    private LocalDateTime fechaHora;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoServicio tipoServicio;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoCita estado = EstadoCita.PROGRAMADA;

    @Column(length = 500)
    private String observaciones;

    // Nombre de quien trae la mascota ese día (dueño o tercero)
    private String nombreQuienTrae;
}
