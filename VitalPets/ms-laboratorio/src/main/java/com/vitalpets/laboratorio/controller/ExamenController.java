package com.vitalpets.laboratorio.controller;

import com.vitalpets.laboratorio.dto.ExamenDto;
import com.vitalpets.laboratorio.model.EstadoExamen;
import com.vitalpets.laboratorio.service.ExamenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/laboratorio")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExamenController {

    private final ExamenService examenService;

    @PostMapping
    public ResponseEntity<ExamenDto> solicitar(@Valid @RequestBody ExamenDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenService.solicitar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ExamenDto>> listar() {
        return ResponseEntity.ok(examenService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamenDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(examenService.buscarPorId(id));
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<ExamenDto>> porMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(examenService.porMascota(mascotaId));
    }

    @GetMapping("/urgentes")
    public ResponseEntity<List<ExamenDto>> urgentes() {
        return ResponseEntity.ok(examenService.urgentes());
    }

    @PatchMapping("/{id}/resultado")
    public ResponseEntity<ExamenDto> cargarResultado(@PathVariable Long id,
                                                    @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(examenService.cargarResultado(id,
                body.get("resultados"), body.get("observaciones")));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ExamenDto> cambiarEstado(@PathVariable Long id,
                                                    @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(examenService.cambiarEstado(id,
                EstadoExamen.valueOf(body.get("estado"))));
    }
}