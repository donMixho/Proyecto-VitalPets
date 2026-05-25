package com.vitalpets.inventario.dto;

import com.vitalpets.inventario.model.CategoriaProducto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductoDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private CategoriaProducto categoria;
    private Integer stockActual;
    private Integer stockMinimo;
    private Double precioUnitario;
    private String unidadMedida;
    private Boolean activo;
    // Campo calculado: indica si el stock está bajo mínimo
    private Boolean stockBajo;
    private Long personalId;
}