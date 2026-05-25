package com.vitalpets.vacunas.dto;

import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VacunaDto {
    private Long id;
    private Long mascotaId;
    private Long clienteId;
    private Long personalId;
    private String nombreVacuna;
    private String laboratorio;
    private String lote;
    private LocalDate fechaAplicacion;
    private LocalDate fechaProximaDosis;
    private String dosis;
    private String observaciones;
    private Boolean vigente;
}