package com.vitalpets.inventario.dto;

import com.vitalpets.inventario.model.CategoriaProducto;
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
@Schema(description = "Datos de un producto en el inventario veterinario")
public class ProductoDto extends RepresentationModel<ProductoDto> {

    @Schema(description = "Identificador único del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Amoxicilina 250mg")
    private String nombre;

    @Schema(description = "Descripción detallada del producto", example = "Antibiótico de amplio espectro")
    private String descripcion;

    @Schema(description = "Categoría: MEDICAMENTO, HERRAMIENTA, ACCESORIO, ALIMENTO, HIGIENE, OTRO", example = "MEDICAMENTO")
    private CategoriaProducto categoria;

    @Schema(description = "Unidades disponibles en bodega", example = "50")
    private Integer stockActual;

    @Schema(description = "Cantidad mínima que activa la alerta de stock bajo", example = "10")
    private Integer stockMinimo;

    @Schema(description = "Precio por unidad de medida en pesos chilenos", example = "1500.0")
    private Double precioUnitario;

    @Schema(description = "Unidad de medida: unidad, caja, frasco, kg", example = "frasco")
    private String unidadMedida;

    @Schema(description = "Indica si el producto está activo en el inventario", example = "true")
    private Boolean activo;

    @Schema(description = "Calculado: true si stockActual <= stockMinimo", example = "false")
    private Boolean stockBajo;

    @Schema(description = "ID del personal responsable del producto", example = "4")
    private Long personalId;
}
