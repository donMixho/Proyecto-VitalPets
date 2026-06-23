package com.vitalpets.usuarios.dto;

import com.vitalpets.usuarios.model.Rol;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class UsuarioDto extends RepresentationModel<UsuarioDto> {

    private Long id;

    private String username;

    private String password;

    private String nombreCompleto;

    private Rol rol;

    private Boolean activo;

    private Long personalId;
}
