package com.vitalpets.personal.dto;

import com.vitalpets.personal.model.Especialidad;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PersonalDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String rut;
    private String telefono;
    private String email;
    private String profesion;
    private Especialidad especialidad;
    private String implementosAsignados;
    private Boolean activo;
    private Long usuarioId;
}