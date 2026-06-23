package com.vitalpets.facturacion.dto;

import com.vitalpets.facturacion.model.EstadoFactura;
import com.vitalpets.facturacion.model.MetodoPago;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class FacturaDto extends RepresentationModel<FacturaDto> {

    private Long id;

    private Long citaId;

    private Long clienteId;

    private Long mascotaId;

    private Long personalId;

    private String nombreQuienTrae;

    private LocalDateTime fechaEmision;

    private EstadoFactura estado;

    private MetodoPago metodoPago;

    private Double totalServicios;

    private Double totalProductos;

    private Double totalFinal;

    private List<DetalleFacturaDto> detalles;
}
