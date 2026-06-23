package com.vitalpets.mascotas.controller;

import com.vitalpets.mascotas.assemblers.MascotaModelAssembler;
import com.vitalpets.mascotas.dto.MascotaDto;
import com.vitalpets.mascotas.model.Especie;
import com.vitalpets.mascotas.model.Mascota;
import com.vitalpets.mascotas.service.MascotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MascotaController {

    private final MascotaService mascotaService;
    private final MascotaModelAssembler assembler;

    @PostMapping
    public ResponseEntity<EntityModel<Mascota>> registrar(@Valid @RequestBody MascotaDto dto) {
        EntityModel<Mascota> model = assembler.toModel(mascotaService.registrar(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Mascota>>> listarActivas() {
        List<EntityModel<Mascota>> mascotas = mascotaService.listarActivas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaController.class).listarActivas()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Mascota>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toModel(mascotaService.buscarPorId(id)));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Mascota>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(mascotaService.buscarPorCliente(clienteId));
    }

    @GetMapping("/especie/{especie}")
    public ResponseEntity<List<Mascota>> buscarPorEspecie(@PathVariable Especie especie) {
        return ResponseEntity.ok(mascotaService.buscarPorEspecie(especie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Mascota>> actualizar(@PathVariable Long id,
            @Valid @RequestBody MascotaDto dto) {
        return ResponseEntity.ok(assembler.toModel(mascotaService.actualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        mascotaService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
