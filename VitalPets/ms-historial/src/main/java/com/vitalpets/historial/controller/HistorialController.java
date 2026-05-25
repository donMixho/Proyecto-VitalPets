package com.vitalpets.historial.controller;

import com.vitalpets.historial.dto.HistorialDto;
import com.vitalpets.historial.service.HistorialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HistorialController {

    private final HistorialService historialService;

    @PostMapping
    public ResponseEntity<HistorialDto> registrar(@Valid @RequestBody HistorialDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historialService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<HistorialDto>> listar() {
        return ResponseEntity.ok(historialService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(historialService.buscarPorId(id));
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<HistorialDto>> porMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(historialService.porMascota(mascotaId));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<HistorialDto>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(historialService.porCliente(clienteId));
    }
}