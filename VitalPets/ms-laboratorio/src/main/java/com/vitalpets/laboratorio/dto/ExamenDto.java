package com.vitalpets.laboratorio.dto;

import com.vitalpets.laboratorio.model.EstadoExamen;
import com.vitalpets.laboratorio.model.TipoExamen;
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
public class ExamenDto extends RepresentationModel<ExamenDto> {

    private Long id;

    private Long mascotaId;

    private Long clienteId;

    private Long personalId;

    private Long citaId;

    private TipoExamen tipoExamen;

    private EstadoExamen estado;

    private LocalDateTime fechaSolicitud;

    private LocalDateTime fechaResultado;

    private String resultados;

    private String observaciones;

    private Boolean urgente;
}
