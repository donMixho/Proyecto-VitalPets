package com.vitalpets.facturacion.model;

import jakarta.persistence.*;
import lombok.*;
import com.vitalpets.facturacion.model.Factura;

@Entity
@Table(name = "detalles_factura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    // Descripción del ítem (ej: "Consulta General", "Amoxicilina 500mg")
    private String descripcion;

    // "SERVICIO" o "PRODUCTO"
    private String tipoItem;

    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}