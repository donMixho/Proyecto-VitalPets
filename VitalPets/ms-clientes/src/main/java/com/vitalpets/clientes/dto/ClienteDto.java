package com.vitalpets.clientes.dto;

import com.vitalpets.clientes.model.TipoDocumento;
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
@Schema(description = "Datos del cliente dueño legal de una o más mascotas")
public class ClienteDto extends RepresentationModel<ClienteDto> {

    @Schema(description = "Identificador único del cliente", example = "1")
    private Long id;

    @Schema(description = "Nombre del cliente", example = "Carlos")
    private String nombre;

    @Schema(description = "Apellido del cliente", example = "Rodríguez")
    private String apellido;

    @Schema(description = "Tipo de documento: RUT, PASAPORTE, CEDULA_EXTRANJERA", example = "RUT")
    private TipoDocumento tipoDocumento;

    @Schema(description = "Número de documento de identidad", example = "12345678-9")
    private String numeroDocumento;

    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String telefono;

    @Schema(description = "Correo electrónico del cliente", example = "carlos@email.com")
    private String email;

    @Schema(description = "Dirección domiciliaria del cliente", example = "Av. Las Condes 1234, Santiago")
    private String direccion;

    @Schema(description = "Indica si el cliente está activo en el sistema", example = "true")
    private Boolean activo;
}
