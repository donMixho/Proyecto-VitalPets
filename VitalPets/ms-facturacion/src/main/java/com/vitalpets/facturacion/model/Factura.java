package com.vitalpets.facturacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long citaId;

    @NotNull
    private Long clienteId;

    @NotNull
    private Long mascotaId;

    @NotNull
    private Long personalId;

    private String nombreQuienTrae;

    @Builder.Default
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoFactura estado = EstadoFactura.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MetodoPago metodoPago = MetodoPago.PENDIENTE;

    private Double totalServicios;
    private Double totalProductos;
    private Double totalFinal;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<DetalleFactura> detalles = new ArrayList<>();
}