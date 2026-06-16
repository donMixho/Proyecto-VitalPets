package com.vitalpets.laboratorio.dto;

import com.vitalpets.laboratorio.model.EstadoExamen;
import com.vitalpets.laboratorio.model.TipoExamen;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Solicitud y resultado de un examen de laboratorio clínico")
public class ExamenDto extends RepresentationModel<ExamenDto> {

    @Schema(description = "Identificador único del examen", example = "1")
    private Long id;

    @Schema(description = "ID de la mascota paciente", example = "3")
    private Long mascotaId;

    @Schema(description = "ID del cliente dueño de la mascota", example = "1")
    private Long clienteId;

    @Schema(description = "ID del profesional que solicitó el examen", example = "7")
    private Long personalId;

    @Schema(description = "ID de la cita vinculada al examen (opcional)", example = "12")
    private Long citaId;

    @Schema(description = "Tipo de examen: HEMOGRAMA, BIOQUIMICA, ORINA, COPROLOGICO, CULTIVO, RADIOGRAFIA, ECOGRAFIA, OTRO",
            example = "HEMOGRAMA")
    private TipoExamen tipoExamen;

    @Schema(description = "Estado: SOLICITADO, EN_PROCESO, COMPLETADO, CANCELADO", example = "SOLICITADO")
    private EstadoExamen estado;

    @Schema(description = "Fecha y hora en que se solicitó el examen", example = "2026-06-15T09:30:00")
    private LocalDateTime fechaSolicitud;

    @Schema(description = "Fecha y hora en que se cargaron los resultados", example = "2026-06-16T14:00:00")
    private LocalDateTime fechaResultado;

    @Schema(description = "Texto con los resultados del examen", example = "Leucocitos: 8.5 x10³/µL — dentro del rango normal")
    private String resultados;

    @Schema(description = "Observaciones del laboratorista o veterinario", example = "Se recomienda repetir en 30 días")
    private String observaciones;

    @Schema(description = "Indica si el examen tiene prioridad urgente", example = "false")
    private Boolean urgente;
}
