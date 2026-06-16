package com.vitalpets.facturacion.dto;

import com.vitalpets.facturacion.model.EstadoFactura;
import com.vitalpets.facturacion.model.MetodoPago;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Factura generada por un servicio o atención veterinaria")
public class FacturaDto extends RepresentationModel<FacturaDto> {

    @Schema(description = "Identificador único de la factura", example = "1")
    private Long id;

    @Schema(description = "ID de la cita vinculada a esta factura", example = "5")
    private Long citaId;

    @Schema(description = "ID del cliente que recibirá la factura", example = "1")
    private Long clienteId;

    @Schema(description = "ID de la mascota atendida", example = "3")
    private Long mascotaId;

    @Schema(description = "ID del profesional que prestó el servicio", example = "7")
    private Long personalId;

    @Schema(description = "Nombre de quien trajo la mascota", example = "Carlos Rodríguez")
    private String nombreQuienTrae;

    @Schema(description = "Fecha y hora de emisión de la factura", example = "2026-06-15T11:00:00")
    private LocalDateTime fechaEmision;

    @Schema(description = "Estado de la factura: PENDIENTE, PAGADA, ANULADA", example = "PENDIENTE")
    private EstadoFactura estado;

    @Schema(description = "Método de pago: EFECTIVO, TARJETA_DEBITO, TARJETA_CREDITO, TRANSFERENCIA", example = "EFECTIVO")
    private MetodoPago metodoPago;

    @Schema(description = "Subtotal de servicios prestados", example = "25000.0")
    private Double totalServicios;

    @Schema(description = "Subtotal de productos utilizados", example = "5000.0")
    private Double totalProductos;

    @Schema(description = "Total final (servicios + productos)", example = "30000.0")
    private Double totalFinal;

    @Schema(description = "Desglose línea a línea de los conceptos facturados")
    private List<DetalleFacturaDto> detalles;
}
