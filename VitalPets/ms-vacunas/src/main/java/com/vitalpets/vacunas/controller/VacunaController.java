package com.vitalpets.vacunas.controller;

import com.vitalpets.vacunas.dto.VacunaDto;
import com.vitalpets.vacunas.service.VacunaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vacunas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VacunaController {

    private final VacunaService vacunaService;

    @PostMapping
    public ResponseEntity<VacunaDto> registrar(@Valid @RequestBody VacunaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vacunaService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<VacunaDto>> listar() {
        return ResponseEntity.ok(vacunaService.listarVigentes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacunaDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vacunaService.buscarPorId(id));
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<VacunaDto>> porMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(vacunaService.porMascota(mascotaId));
    }

    @GetMapping("/proximas-a-vencer")
    public ResponseEntity<List<VacunaDto>> proximasAVencer() {
        return ResponseEntity.ok(vacunaService.proximasAVencer());
    }
}