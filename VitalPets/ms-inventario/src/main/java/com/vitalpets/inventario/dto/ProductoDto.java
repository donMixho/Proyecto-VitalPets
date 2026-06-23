package com.vitalpets.inventario.dto;

import com.vitalpets.inventario.model.CategoriaProducto;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class ProductoDto extends RepresentationModel<ProductoDto> {

    private Long id;

    private String nombre;

    private String descripcion;

    private CategoriaProducto categoria;

    private Integer stockActual;

    private Integer stockMinimo;

    private Double precioUnitario;

    private String unidadMedida;

    private Boolean activo;

    private Boolean stockBajo;

    private Long personalId;
}
