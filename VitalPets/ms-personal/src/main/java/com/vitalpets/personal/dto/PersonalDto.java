package com.vitalpets.personal.dto;

import com.vitalpets.personal.model.Especialidad;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Datos de un integrante del equipo veterinario")
public class PersonalDto extends RepresentationModel<PersonalDto> {

    @Schema(description = "Identificador único del integrante", example = "1")
    private Long id;

    @Schema(description = "Nombre del integrante", example = "Sofía")
    private String nombre;

    @Schema(description = "Apellido del integrante", example = "Morales")
    private String apellido;

    @Schema(description = "RUT chileno del integrante", example = "15234567-8")
    private String rut;

    @Schema(description = "Teléfono de contacto", example = "+56987654321")
    private String telefono;

    @Schema(description = "Correo electrónico institucional", example = "sofia.morales@vitalpets.cl")
    private String email;

    @Schema(description = "Profesión o título", example = "Médico Veterinario")
    private String profesion;

    @Schema(description = "Especialidad: VETERINARIO_GENERAL, CIRUJANO, ESTILISTA_CANINO, LABORATORISTA, ADMINISTRATIVO, OTRO",
            example = "VETERINARIO_GENERAL")
    private Especialidad especialidad;

    @Schema(description = "Listado de implementos o herramientas asignadas", example = "Estetoscopio, otoscopio, guantes")
    private String implementosAsignados;

    @Schema(description = "Indica si el integrante está activo", example = "true")
    private Boolean activo;

    @Schema(description = "ID del usuario de sistema vinculado al integrante", example = "2")
    private Long usuarioId;
}
