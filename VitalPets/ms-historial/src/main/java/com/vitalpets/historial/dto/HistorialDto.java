package com.vitalpets.historial.dto;

import com.vitalpets.historial.model.TipoEvento;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class HistorialDto extends RepresentationModel<HistorialDto> {

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
