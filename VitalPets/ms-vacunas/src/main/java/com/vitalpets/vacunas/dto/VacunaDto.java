package com.vitalpets.vacunas.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class VacunaDto extends RepresentationModel<VacunaDto> {

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
