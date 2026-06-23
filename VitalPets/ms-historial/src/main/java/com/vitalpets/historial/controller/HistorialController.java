package com.vitalpets.historial.controller;

import com.vitalpets.historial.dto.HistorialDto;
import com.vitalpets.historial.service.HistorialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HistorialController {

    private final HistorialService historialService;

    @PostMapping
    public ResponseEntity<HistorialDto> registrar(@Valid @RequestBody HistorialDto dto) {
        HistorialDto response = historialService.registrar(dto);
        response.add(linkTo(methodOn(HistorialController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(HistorialController.class).listar()).withRel("historial"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<HistorialDto>> listar() {
        List<HistorialDto> lista = historialService.listarTodos();
        lista.forEach(h -> h.add(
            linkTo(methodOn(HistorialController.class).buscarPorId(h.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(HistorialController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialDto> buscarPorId(@PathVariable Long id) {
        HistorialDto response = historialService.buscarPorId(id);
        response.add(linkTo(methodOn(HistorialController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(HistorialController.class).listar()).withRel("historial"));
        return ResponseEntity.ok(response);
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
