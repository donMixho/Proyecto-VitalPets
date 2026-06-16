package com.vitalpets.citas.dto;

import com.vitalpets.citas.model.EstadoCita;
import com.vitalpets.citas.model.TipoServicio;
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
@Schema(description = "Datos de una cita veterinaria agendada")
public class CitaDto extends RepresentationModel<CitaDto> {

    @Schema(description = "Identificador único de la cita", example = "1")
    private Long id;

    @Schema(description = "ID de la mascota que asistirá a la cita", example = "3")
    private Long mascotaId;

    @Schema(description = "ID del cliente dueño de la mascota", example = "1")
    private Long clienteId;

    @Schema(description = "ID del tercero autorizado que trae la mascota (opcional)", example = "2")
    private Long terceroId;

    @Schema(description = "ID del profesional asignado a la cita", example = "7")
    private Long personalId;

    @Schema(description = "Fecha y hora de la cita en formato ISO-8601", example = "2026-06-20T10:30:00")
    private LocalDateTime fechaHora;

    @Schema(description = "Tipo de servicio: CONSULTA_GENERAL, CIRUGIA, GROOMING, VACUNACION, LABORATORIO, OTRO", example = "CONSULTA_GENERAL")
    private TipoServicio tipoServicio;

    @Schema(description = "Estado actual: PROGRAMADA, CONFIRMADA, EN_CURSO, COMPLETADA, CANCELADA", example = "PROGRAMADA")
    private EstadoCita estado;

    @Schema(description = "Observaciones adicionales sobre la cita", example = "Mascota con fobia a otros perros")
    private String observaciones;

    @Schema(description = "Nombre completo de quien trae la mascota", example = "Ana Pérez")
    private String nombreQuienTrae;
}
