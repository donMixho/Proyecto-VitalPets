package com.vitalpets.facturacion.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DetalleFacturaDto {
    private Long id;
    private String descripcion;
    private String tipoItem;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
