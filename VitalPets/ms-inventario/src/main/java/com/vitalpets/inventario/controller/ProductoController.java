package com.vitalpets.inventario.controller;

import com.vitalpets.inventario.dto.ProductoDto;
import com.vitalpets.inventario.model.CategoriaProducto;
import com.vitalpets.inventario.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoDto> registrar(@Valid @RequestBody ProductoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductoDto>> listar() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
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