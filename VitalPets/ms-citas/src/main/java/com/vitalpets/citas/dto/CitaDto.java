package com.vitalpets.citas.dto;

import com.vitalpets.citas.model.EstadoCita;
import com.vitalpets.citas.model.TipoServicio;
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
public class CitaDto extends RepresentationModel<CitaDto> {

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
