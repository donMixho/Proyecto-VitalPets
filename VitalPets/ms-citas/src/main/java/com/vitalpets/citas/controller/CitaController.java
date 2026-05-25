package com.vitalpets.citas.controller;

import com.vitalpets.citas.dto.CitaDto;
import com.vitalpets.citas.model.EstadoCita;
import com.vitalpets.citas.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<CitaDto> registrar(@Valid @RequestBody CitaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<CitaDto>> listar() {
        return ResponseEntity.ok(citaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CitaDto>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(citaService.porCliente(clienteId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CitaDto>> porEstado(@PathVariable EstadoCita estado) {
        return ResponseEntity.ok(citaService.porEstado(estado));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<CitaDto> cambiarEstado(@PathVariable Long id,
                                                @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(citaService.cambiarEstado(id,
                EstadoCita.valueOf(body.get("estado"))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        citaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}