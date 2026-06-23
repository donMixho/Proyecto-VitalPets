package com.vitalpets.laboratorio.controller;

import com.vitalpets.laboratorio.dto.ExamenDto;
import com.vitalpets.laboratorio.model.EstadoExamen;
import com.vitalpets.laboratorio.service.ExamenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/laboratorio")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExamenController {

    private final ExamenService examenService;

    @PostMapping
    public ResponseEntity<ExamenDto> solicitar(@Valid @RequestBody ExamenDto dto) {
        ExamenDto response = examenService.solicitar(dto);
        response.add(linkTo(methodOn(ExamenController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(ExamenController.class).listar()).withRel("laboratorio"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<ExamenDto>> listar() {
        List<ExamenDto> lista = examenService.listarTodos();
        lista.forEach(e -> e.add(
            linkTo(methodOn(ExamenController.class).buscarPorId(e.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(ExamenController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamenDto> buscarPorId(@PathVariable Long id) {
        ExamenDto response = examenService.buscarPorId(id);
        response.add(linkTo(methodOn(ExamenController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(ExamenController.class).listar()).withRel("laboratorio"));
        return ResponseEntity.ok(response);
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
