package com.vitalpets.usuarios.dto;

import com.vitalpets.usuarios.model.Rol;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioDto {
    private Long id;
    private String username;
    private String password;
    private String nombreCompleto;
    private Rol rol;
    private Boolean activo;
    private Long personalId;
}