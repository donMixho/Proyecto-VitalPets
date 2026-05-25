package com.vitalpets.citas.dto;

import com.vitalpets.citas.model.EstadoCita;
import com.vitalpets.citas.model.TipoServicio;
import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CitaDto {
    private Long id;
    private Long mascotaId;
    private Long clienteId;
    private Long terceroId;
    private Long personalId;
    private LocalDateTime fechaHora;
    private TipoServicio tipoServicio;
    private EstadoCita estado;
    private String observaciones;
    private String nombreQuienTrae;
}