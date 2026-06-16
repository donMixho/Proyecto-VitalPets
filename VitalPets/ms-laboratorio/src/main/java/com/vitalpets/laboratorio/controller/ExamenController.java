package com.vitalpets.laboratorio.controller;

import com.vitalpets.laboratorio.dto.ExamenDto;
import com.vitalpets.laboratorio.model.EstadoExamen;
import com.vitalpets.laboratorio.service.ExamenService;
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
@RequestMapping("/api/laboratorio")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Laboratorio", description = "Solicitud y registro de resultados de exámenes clínicos")
public class ExamenController {

    private final ExamenService examenService;

    @Operation(summary = "Solicitar examen de laboratorio", description = "Crea la solicitud validando que la mascota exista")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Examen solicitado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @PostMapping
    public ResponseEntity<ExamenDto> solicitar(@Valid @RequestBody ExamenDto dto) {
        ExamenDto response = examenService.solicitar(dto);
        response.add(linkTo(methodOn(ExamenController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(ExamenController.class).listar()).withRel("laboratorio"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos los exámenes", description = "Retorna el listado completo de exámenes con enlaces HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<ExamenDto>> listar() {
        List<ExamenDto> lista = examenService.listarTodos();
        lista.forEach(e -> e.add(
            linkTo(methodOn(ExamenController.class).buscarPorId(e.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(ExamenController.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Buscar examen por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Examen encontrado"),
        @ApiResponse(responseCode = "404", description = "Examen no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExamenDto> buscarPorId(@PathVariable Long id) {
        ExamenDto response = examenService.buscarPorId(id);
        response.add(linkTo(methodOn(ExamenController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(ExamenController.class).listar()).withRel("laboratorio"));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Exámenes de una mascota")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<ExamenDto>> porMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(examenService.porMascota(mascotaId));
    }

    @Operation(summary = "Listar exámenes urgentes", description = "Retorna exámenes marcados como urgente=true")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/urgentes")
    public ResponseEntity<List<ExamenDto>> urgentes() {
        return ResponseEntity.ok(examenService.urgentes());
    }

    @Operation(summary = "Cargar resultado de examen", description = "Body: { \"resultados\": \"...\", \"observaciones\": \"...\" }. Cambia estado a COMPLETADO")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado cargado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Examen no encontrado")
    })
    @PatchMapping("/{id}/resultado")
    public ResponseEntity<ExamenDto> cargarResultado(@PathVariable Long id,
                                                    @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(examenService.cargarResultado(id,
                body.get("resultados"), body.get("observaciones")));
    }

    @Operation(summary = "Cambiar estado del examen", description = "Body: { \"estado\": \"EN_PROCESO\" }")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Examen no encontrado")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ExamenDto> cambiarEstado(@PathVariable Long id,
                                                    @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(examenService.cambiarEstado(id,
                EstadoExamen.valueOf(body.get("estado"))));
    }
}
