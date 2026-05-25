package com.vitalpets.clientes.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TerceroDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private Long clienteId;
    private String relacion;
    private Boolean activo;
}