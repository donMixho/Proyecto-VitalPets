package com.vitalpets.historial.dto;

import com.vitalpets.historial.model.TipoEvento;
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
@Schema(description = "Registro de un evento médico en el historial clínico de una mascota")
public class HistorialDto extends RepresentationModel<HistorialDto> {

    @Schema(description = "Identificador único del registro", example = "1")
    private Long id;

    @Schema(description = "ID de la mascota paciente", example = "3")
    private Long mascotaId;

    @Schema(description = "ID del cliente dueño de la mascota", example = "1")
    private Long clienteId;

    @Schema(description = "ID del profesional que atendió", example = "7")
    private Long personalId;

    @Schema(description = "ID de la cita asociada al evento (opcional)", example = "12")
    private Long citaId;

    @Schema(description = "Tipo de evento: CONSULTA, CIRUGIA, VACUNACION, EXAMEN, GROOMING, OTRO", example = "CONSULTA")
    private TipoEvento tipoEvento;

    @Schema(description = "Fecha y hora del evento", example = "2026-06-15T09:00:00")
    private LocalDateTime fechaEvento;

    @Schema(description = "Descripción general del evento", example = "Revisión de rutina anual")
    private String descripcion;

    @Schema(description = "Diagnóstico emitido por el veterinario", example = "Otitis externa leve")
    private String diagnostico;

    @Schema(description = "Tratamiento indicado", example = "Gotas óticas cada 12 horas por 7 días")
    private String tratamiento;

    @Schema(description = "Medicamentos recetados", example = "Otozyme 10ml")
    private String medicamentosRecetados;

    @Schema(description = "Fecha sugerida para próxima visita", example = "2026-07-15")
    private String proximaVisita;

    @Schema(description = "Nombre de quien trajo la mascota", example = "Carlos Rodríguez")
    private String nombreQuienTrajo;
}
