package com.vitalpets.inventario.controller;

import com.vitalpets.inventario.dto.ProductoDto;
import com.vitalpets.inventario.model.CategoriaProducto;
import com.vitalpets.inventario.service.ProductoService;
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
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoDto> registrar(@Valid @RequestBody ProductoDto dto) {
        ProductoDto response = productoService.registrar(dto);
        response.add(linkTo(methodOn(ProductoController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(ProductoController.class).listar()).withRel("inventario"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<ProductoDto>> listar() {
        List<ProductoDto> lista = productoService.listarActivos();
        lista.forEach(p -> p.add(
            linkTo(methodOn(ProductoController.class).buscarPorId(p.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(ProductoController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> buscarPorId(@PathVariable Long id) {
        ProductoDto response = productoService.buscarPorId(id);
        response.add(linkTo(methodOn(ProductoController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(ProductoController.class).listar()).withRel("inventario"));
        response.add(linkTo(methodOn(ProductoController.class).desactivar(id)).withRel("delete"));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDto>> porCategoria(@PathVariable CategoriaProducto categoria) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(categoria));
    }

    @GetMapping("/alertas")
    public ResponseEntity<List<ProductoDto>> stockBajo() {
        return ResponseEntity.ok(productoService.alertasStockBajo());
    }

    @PatchMapping("/{id}/reducir")
    public ResponseEntity<ProductoDto> reducir(@PathVariable Long id,
                                                @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(productoService.reducirStock(id, body.get("cantidad")));
    }

    @PatchMapping("/{id}/aumentar")
    public ResponseEntity<ProductoDto> aumentar(@PathVariable Long id,
                                                @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(productoService.aumentarStock(id, body.get("cantidad")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        productoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
