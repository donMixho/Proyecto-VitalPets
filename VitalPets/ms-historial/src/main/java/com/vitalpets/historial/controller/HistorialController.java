package com.vitalpets.historial.controller;

import com.vitalpets.historial.dto.HistorialDto;
import com.vitalpets.historial.service.HistorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Historial Médico", description = "Registro y consulta del historial clínico completo por mascota")
public class HistorialController {

    private final HistorialService historialService;

    @Operation(summary = "Registrar evento médico", description = "Crea un registro en el historial validando que la mascota exista")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Registro creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @PostMapping
    public ResponseEntity<HistorialDto> registrar(@Valid @RequestBody HistorialDto dto) {
        HistorialDto response = historialService.registrar(dto);
        response.add(linkTo(methodOn(HistorialController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(HistorialController.class).listar()).withRel("historial"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos los registros del historial", description = "Retorna el historial completo de todos los pacientes")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<HistorialDto>> listar() {
        List<HistorialDto> lista = historialService.listarTodos();
        lista.forEach(h -> h.add(
            linkTo(methodOn(HistorialController.class).buscarPorId(h.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(HistorialController.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Buscar registro de historial por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro encontrado"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HistorialDto> buscarPorId(@PathVariable Long id) {
        HistorialDto response = historialService.buscarPorId(id);
        response.add(linkTo(methodOn(HistorialController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(HistorialController.class).listar()).withRel("historial"));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Historial médico de una mascota", description = "Retorna los eventos ordenados por fecha descendente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente")
    })
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<HistorialDto>> porMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(historialService.porMascota(mascotaId));
    }

    @Operation(summary = "Historial médico por cliente", description = "Retorna todos los eventos de todas las mascotas del cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<HistorialDto>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(historialService.porCliente(clienteId));
    }
}
