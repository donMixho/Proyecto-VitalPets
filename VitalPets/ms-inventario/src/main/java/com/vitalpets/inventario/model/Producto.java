package com.vitalpets.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CategoriaProducto categoria;

    // Stock actual disponible
    @NotNull
    @Min(0)
    private Integer stockActual;

    // Alerta cuando el stock baja de este número
    @NotNull
    @Min(0)
    private Integer stockMinimo;

    // Precio unitario de venta al cliente
    @NotNull
    @Min(0)
    private Double precioUnitario;

    private String unidadMedida; // "unidad", "caja", "ml", "mg"

    @Builder.Default
    private Boolean activo = true;
}