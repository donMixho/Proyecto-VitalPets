package com.vitalpets.mascotas.controller;

import com.vitalpets.mascotas.dto.MascotaDto;
import com.vitalpets.mascotas.model.Especie;
import com.vitalpets.mascotas.service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mascotas")  // URL base de este microservicio
@RequiredArgsConstructor
@CrossOrigin(origins = "*")       // Permite llamadas desde el frontend HTML
public class MascotaController {

    private final MascotaService mascotaService;

    // ── POST /api/mascotas ────────────────────────────────────
    // Registrar una mascota nueva
    @PostMapping
    public ResponseEntity<MascotaDto> registrar(@Valid @RequestBody MascotaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mascotaService.registrar(dto));
    }

    // ── GET /api/mascotas ─────────────────────────────────────
    // Listar todas las mascotas activas
    @GetMapping
    public ResponseEntity<List<MascotaDto>> listarActivas() {
        return ResponseEntity.ok(mascotaService.listarActivas());
    }

    // ── GET /api/mascotas/{id} ────────────────────────────────
    // Buscar mascota por su ID
    @GetMapping("/{id}")
    public ResponseEntity<MascotaDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.buscarPorId(id));
    }

    // ── GET /api/mascotas/cliente/{clienteId} ─────────────────
    // Buscar todas las mascotas de un dueño
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<MascotaDto>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(mascotaService.buscarPorCliente(clienteId));
    }

    // ── GET /api/mascotas/especie/{especie} ───────────────────
    // Buscar mascotas por especie (ej: /especie/REPTIL)
    @GetMapping("/especie/{especie}")
    public ResponseEntity<List<MascotaDto>> buscarPorEspecie(@PathVariable Especie especie) {
        return ResponseEntity.ok(mascotaService.buscarPorEspecie(especie));
    }

    // ── PUT /api/mascotas/{id} ────────────────────────────────
    // Actualizar datos de una mascota
    @PutMapping("/{id}")
    public ResponseEntity<MascotaDto> actualizar(@PathVariable Long id,
        @Valid @RequestBody MascotaDto dto) {
        return ResponseEntity.ok(mascotaService.actualizar(id, dto));
    }

    // ── DELETE /api/mascotas/{id} ─────────────────────────────
    // Desactivar mascota (borrado lógico, no se elimina de la BD)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        mascotaService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}