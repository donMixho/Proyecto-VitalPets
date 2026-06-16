package com.vitalpets.vacunas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Registro de aplicación de una vacuna a una mascota")
public class VacunaDto extends RepresentationModel<VacunaDto> {

    @Schema(description = "Identificador único del registro de vacuna", example = "1")
    private Long id;

    @Schema(description = "ID de la mascota vacunada", example = "3")
    private Long mascotaId;

    @Schema(description = "ID del cliente dueño de la mascota", example = "1")
    private Long clienteId;

    @Schema(description = "ID del profesional que aplicó la vacuna", example = "7")
    private Long personalId;

    @Schema(description = "Nombre comercial de la vacuna", example = "Nobivac Puppy DP")
    private String nombreVacuna;

    @Schema(description = "Laboratorio fabricante", example = "Merck Animal Health")
    private String laboratorio;

    @Schema(description = "Número de lote del vial", example = "LOT-2026-0542")
    private String lote;

    @Schema(description = "Fecha de aplicación de la vacuna", example = "2026-06-15")
    private LocalDate fechaAplicacion;

    @Schema(description = "Fecha en que se debe aplicar la próxima dosis", example = "2027-06-15")
    private LocalDate fechaProximaDosis;

    @Schema(description = "Número o descripción de la dosis aplicada", example = "Dosis inicial")
    private String dosis;

    @Schema(description = "Observaciones clínicas post-aplicación", example = "Sin reacciones adversas")
    private String observaciones;

    @Schema(description = "Indica si la vacunación sigue vigente (no ha expirado)", example = "true")
    private Boolean vigente;
}
