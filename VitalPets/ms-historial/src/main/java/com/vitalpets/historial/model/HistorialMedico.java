package com.vitalpets.historial.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_medico")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class HistorialMedico {

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
    private TipoEvento tipoEvento;

    @Builder.Default
    private LocalDateTime fechaEvento = LocalDateTime.now();

    @NotBlank
    @Column(length = 1000)
    private String descripcion;

    // Diagnóstico del veterinario
    @Column(length = 500)
    private String diagnostico;

    // Tratamiento indicado
    @Column(length = 500)
    private String tratamiento;

    // Medicamentos recetados
    @Column(length = 500)
    private String medicamentosRecetados;

    // Próxima visita recomendada
    private String proximaVisita;

    private String nombreQuienTrajo;
}
