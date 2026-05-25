package com.vitalpets.clientes.dto;

import com.vitalpets.clientes.model.TipoDocumento;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDto {
    private Long id;
    private String nombre;
    private String apellido;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String telefono;
    private String email;
    private String direccion;
    private Boolean activo;
}