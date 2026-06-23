package com.vitalpets.clientes.dto;

import com.vitalpets.clientes.model.TipoDocumento;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class ClienteDto extends RepresentationModel<ClienteDto> {

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
