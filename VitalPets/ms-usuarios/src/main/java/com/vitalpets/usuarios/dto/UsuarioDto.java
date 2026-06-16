package com.vitalpets.usuarios.dto;

import com.vitalpets.usuarios.model.Rol;
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
@Schema(description = "Cuenta de acceso al sistema VitalPets con rol asignado")
public class UsuarioDto extends RepresentationModel<UsuarioDto> {

    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario para iniciar sesión", example = "admin")
    private String username;

    @Schema(description = "Contraseña del usuario (no se retorna en consultas GET)", example = "secreto123")
    private String password;

    @Schema(description = "Nombre completo del usuario", example = "Leandro Ruiz")
    private String nombreCompleto;

    @Schema(description = "Rol en el sistema: ADMIN, VETERINARIO, RECEPCIONISTA, LABORATORISTA", example = "ADMIN")
    private Rol rol;

    @Schema(description = "Indica si la cuenta está activa", example = "true")
    private Boolean activo;

    @Schema(description = "ID del integrante del personal vinculado a esta cuenta", example = "3")
    private Long personalId;
}
