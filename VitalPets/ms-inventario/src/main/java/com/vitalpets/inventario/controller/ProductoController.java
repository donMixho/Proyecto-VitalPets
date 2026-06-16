package com.vitalpets.inventario.controller;

import com.vitalpets.inventario.dto.ProductoDto;
import com.vitalpets.inventario.model.CategoriaProducto;
import com.vitalpets.inventario.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Inventario", description = "Control de medicamentos y productos con alertas de stock mínimo")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Registrar nuevo producto", description = "Agrega un producto al inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<ProductoDto> registrar(@Valid @RequestBody ProductoDto dto) {
        ProductoDto response = productoService.registrar(dto);
        response.add(linkTo(methodOn(ProductoController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(ProductoController.class).listar()).withRel("inventario"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar productos activos", description = "Retorna todos los productos con activo=true y sus links HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<ProductoDto>> listar() {
        List<ProductoDto> lista = productoService.listarActivos();
        lista.forEach(p -> p.add(
            linkTo(methodOn(ProductoController.class).buscarPorId(p.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(ProductoController.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Buscar producto por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> buscarPorId(@PathVariable Long id) {
        ProductoDto response = productoService.buscarPorId(id);
        response.add(linkTo(methodOn(ProductoController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(ProductoController.class).listar()).withRel("inventario"));
        response.add(linkTo(methodOn(ProductoController.class).desactivar(id)).withRel("delete"));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filtrar productos por categoría", description = "Categorías: MEDICAMENTO, HERRAMIENTA, ACCESORIO, ALIMENTO, HIGIENE, OTRO")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDto>> porCategoria(@PathVariable CategoriaProducto categoria) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(categoria));
    }

    @Operation(summary = "Productos con stock bajo", description = "Retorna productos cuyo stock actual es menor o igual al stock mínimo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de alertas obtenida exitosamente")
    })
    @GetMapping("/alertas")
    public ResponseEntity<List<ProductoDto>> stockBajo() {
        return ResponseEntity.ok(productoService.alertasStockBajo());
    }

    @Operation(summary = "Reducir stock de un producto", description = "Body: { \"cantidad\": 5 }. Lanza error si stock insuficiente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock reducido correctamente"),
        @ApiResponse(responseCode = "400", description = "Stock insuficiente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PatchMapping("/{id}/reducir")
    public ResponseEntity<ProductoDto> reducir(@PathVariable Long id,
                                                @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(productoService.reducirStock(id, body.get("cantidad")));
    }

    @Operation(summary = "Aumentar stock de un producto", description = "Body: { \"cantidad\": 10 }")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock aumentado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PatchMapping("/{id}/aumentar")
    public ResponseEntity<ProductoDto> aumentar(@PathVariable Long id,
                                                @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(productoService.aumentarStock(id, body.get("cantidad")));
    }

    @Operation(summary = "Desactivar producto (soft delete)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto desactivado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        productoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
