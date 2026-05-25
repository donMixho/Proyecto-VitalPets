package com.vitalpets.facturacion.dto;

import com.vitalpets.facturacion.model.EstadoFactura;
import com.vitalpets.facturacion.model.MetodoPago;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class FacturaDto {
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