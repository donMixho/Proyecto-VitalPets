package com.vitalpets.vacunas.controller;

import com.vitalpets.vacunas.dto.VacunaDto;
import com.vitalpets.vacunas.service.VacunaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/vacunas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VacunaController {

    private final VacunaService vacunaService;

    @PostMapping
    public ResponseEntity<VacunaDto> registrar(@Valid @RequestBody VacunaDto dto) {
        VacunaDto response = vacunaService.registrar(dto);
        response.add(linkTo(methodOn(VacunaController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(VacunaController.class).listar()).withRel("vacunas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<VacunaDto>> listar() {
        List<VacunaDto> lista = vacunaService.listarVigentes();
        lista.forEach(v -> v.add(
            linkTo(methodOn(VacunaController.class).buscarPorId(v.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(VacunaController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacunaDto> buscarPorId(@PathVariable Long id) {
        VacunaDto response = vacunaService.buscarPorId(id);
        response.add(linkTo(methodOn(VacunaController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(VacunaController.class).listar()).withRel("vacunas"));
        return ResponseEntity.ok(response);
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
