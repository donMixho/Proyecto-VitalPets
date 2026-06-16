package com.vitalpets.citas.controller;

import com.vitalpets.citas.dto.CitaDto;
import com.vitalpets.citas.model.EstadoCita;
import com.vitalpets.citas.service.CitaService;
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
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Citas", description = "Agenda y gestión de consultas y servicios veterinarios")
public class CitaController {

    private final CitaService citaService;

    @Operation(summary = "Registrar nueva cita", description = "Agenda una cita validando que la mascota y el cliente existan")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cita registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Mascota o cliente no encontrado")
    })
    @PostMapping
    public ResponseEntity<CitaDto> registrar(@Valid @RequestBody CitaDto dto) {
        CitaDto response = citaService.registrar(dto);
        response.add(linkTo(methodOn(CitaController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(CitaController.class).listar()).withRel("citas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todas las citas", description = "Retorna la agenda completa con enlaces HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<CitaDto>> listar() {
        List<CitaDto> lista = citaService.listarTodas();
        lista.forEach(c -> c.add(
            linkTo(methodOn(CitaController.class).buscarPorId(c.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(CitaController.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Buscar cita por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cita encontrada"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CitaDto> buscarPorId(@PathVariable Long id) {
        CitaDto response = citaService.buscarPorId(id);
        response.add(linkTo(methodOn(CitaController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(CitaController.class).listar()).withRel("citas"));
        response.add(linkTo(methodOn(CitaController.class).cancelar(id)).withRel("delete"));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar citas de un cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CitaDto>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(citaService.porCliente(clienteId));
    }

    @Operation(summary = "Listar citas por estado", description = "Filtra por estado: PROGRAMADA, CONFIRMADA, EN_CURSO, COMPLETADA, CANCELADA")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CitaDto>> porEstado(@PathVariable EstadoCita estado) {
        return ResponseEntity.ok(citaService.porEstado(estado));
    }

    @Operation(summary = "Cambiar estado de una cita", description = "Body: { \"estado\": \"CONFIRMADA\" }")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CitaDto> cambiarEstado(@PathVariable Long id,
                                                @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(citaService.cambiarEstado(id,
                EstadoCita.valueOf(body.get("estado"))));
    }

    @Operation(summary = "Cancelar cita (soft delete)", description = "Cambia el estado de la cita a CANCELADA")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cita cancelada correctamente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        citaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
