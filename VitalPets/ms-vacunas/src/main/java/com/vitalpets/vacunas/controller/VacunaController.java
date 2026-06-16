package com.vitalpets.vacunas.controller;

import com.vitalpets.vacunas.dto.VacunaDto;
import com.vitalpets.vacunas.service.VacunaService;
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
@RequestMapping("/api/vacunas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Vacunas", description = "Control del calendario de vacunación y alertas de dosis próximas a vencer")
public class VacunaController {

    private final VacunaService vacunaService;

    @Operation(summary = "Registrar aplicación de vacuna", description = "Crea el registro validando que la mascota exista en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vacuna registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @PostMapping
    public ResponseEntity<VacunaDto> registrar(@Valid @RequestBody VacunaDto dto) {
        VacunaDto response = vacunaService.registrar(dto);
        response.add(linkTo(methodOn(VacunaController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(VacunaController.class).listar()).withRel("vacunas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar vacunas vigentes", description = "Retorna todas las vacunas con vigente=true y enlaces HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<VacunaDto>> listar() {
        List<VacunaDto> lista = vacunaService.listarVigentes();
        lista.forEach(v -> v.add(
            linkTo(methodOn(VacunaController.class).buscarPorId(v.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(VacunaController.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Buscar vacuna por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vacuna encontrada"),
        @ApiResponse(responseCode = "404", description = "Vacuna no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VacunaDto> buscarPorId(@PathVariable Long id) {
        VacunaDto response = vacunaService.buscarPorId(id);
        response.add(linkTo(methodOn(VacunaController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(VacunaController.class).listar()).withRel("vacunas"));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Historial de vacunación de una mascota")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente")
    })
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<VacunaDto>> porMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(vacunaService.porMascota(mascotaId));
    }

    @Operation(summary = "Vacunas próximas a vencer", description = "Retorna vacunas vigentes cuya próxima dosis vence en los próximos 30 días")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de alertas obtenida exitosamente")
    })
    @GetMapping("/proximas-a-vencer")
    public ResponseEntity<List<VacunaDto>> proximasAVencer() {
        return ResponseEntity.ok(vacunaService.proximasAVencer());
    }
}
