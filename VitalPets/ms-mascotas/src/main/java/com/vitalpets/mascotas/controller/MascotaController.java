package com.vitalpets.mascotas.controller;

import com.vitalpets.mascotas.assemblers.MascotaModelAssembler;
import com.vitalpets.mascotas.dto.MascotaDto;
import com.vitalpets.mascotas.model.Especie;
import com.vitalpets.mascotas.model.Mascota;
import com.vitalpets.mascotas.service.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Mascotas", description = "Operaciones CRUD para gestión de mascotas de compañía y exóticas")
public class MascotaController {

    private final MascotaService mascotaService;
    private final MascotaModelAssembler assembler;

    @Operation(summary = "Registrar nueva mascota", description = "Crea una mascota y la asocia al cliente dueño")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Mascota creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Mascota>> registrar(@Valid @RequestBody MascotaDto dto) {
        EntityModel<Mascota> model = assembler.toModel(mascotaService.registrar(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @Operation(summary = "Listar mascotas activas", description = "Retorna todas las mascotas con activo=true y enlaces HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Mascota>>> listarActivas() {
        List<EntityModel<Mascota>> mascotas = mascotaService.listarActivas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaController.class).listarActivas()).withSelfRel()));
    }

    @Operation(summary = "Buscar mascota por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mascota encontrada"),
        @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Mascota>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toModel(mascotaService.buscarPorId(id)));
    }

    @Operation(summary = "Listar mascotas por cliente", description = "Retorna todas las mascotas asociadas al clienteId indicado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Mascota>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(mascotaService.buscarPorCliente(clienteId));
    }

    @Operation(summary = "Buscar mascotas por especie", description = "Filtra mascotas por especie (PERRO, GATO, AVE, etc.)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/especie/{especie}")
    public ResponseEntity<List<Mascota>> buscarPorEspecie(@PathVariable Especie especie) {
        return ResponseEntity.ok(mascotaService.buscarPorEspecie(especie));
    }

    @Operation(summary = "Actualizar datos de una mascota")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mascota actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Mascota no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Mascota>> actualizar(@PathVariable Long id,
            @Valid @RequestBody MascotaDto dto) {
        return ResponseEntity.ok(assembler.toModel(mascotaService.actualizar(id, dto)));
    }

    @Operation(summary = "Desactivar mascota (soft delete)", description = "Marca la mascota como inactiva sin eliminarla de la base de datos")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Mascota desactivada correctamente"),
        @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        mascotaService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
