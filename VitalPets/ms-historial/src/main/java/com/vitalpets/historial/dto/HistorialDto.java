package com.vitalpets.historial.dto;

import com.vitalpets.historial.model.TipoEvento;
import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class HistorialDto {
    private Long id;
    private Long mascotaId;
    private Long clienteId;
    private Long personalId;
    private Long citaId;
    private TipoEvento tipoEvento;
    private LocalDateTime fechaEvento;
    private String descripcion;
    private String diagnostico;
    private String tratamiento;
    private String medicamentosRecetados;
    private String proximaVisita;
    private String nombreQuienTrajo;
}