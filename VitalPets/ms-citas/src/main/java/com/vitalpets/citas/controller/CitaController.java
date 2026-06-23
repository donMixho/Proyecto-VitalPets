package com.vitalpets.citas.controller;

import com.vitalpets.citas.dto.CitaDto;
import com.vitalpets.citas.model.EstadoCita;
import com.vitalpets.citas.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<CitaDto> registrar(@Valid @RequestBody CitaDto dto) {
        CitaDto response = citaService.registrar(dto);
        response.add(linkTo(methodOn(CitaController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(CitaController.class).listar()).withRel("citas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<CitaDto>> listar() {
        List<CitaDto> lista = citaService.listarTodas();
        lista.forEach(c -> c.add(
            linkTo(methodOn(CitaController.class).buscarPorId(c.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(CitaController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaDto> buscarPorId(@PathVariable Long id) {
        CitaDto response = citaService.buscarPorId(id);
        response.add(linkTo(methodOn(CitaController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(CitaController.class).listar()).withRel("citas"));
        response.add(linkTo(methodOn(CitaController.class).cancelar(id)).withRel("delete"));
        return ResponseEntity.ok(response);
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
