package com.vitalpets.personal.controller;

import com.vitalpets.personal.dto.PersonalDto;
import com.vitalpets.personal.model.Especialidad;
import com.vitalpets.personal.service.PersonalService;
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
@RequestMapping("/api/personal")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Personal", description = "Gestión del equipo veterinario: veterinarios, estilistas, técnicos y administrativos")
public class PersonalController {

    private final PersonalService personalService;

    @Operation(summary = "Registrar nuevo integrante del personal")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Personal registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<PersonalDto> registrar(@Valid @RequestBody PersonalDto dto) {
        PersonalDto response = personalService.registrar(dto);
        response.add(linkTo(methodOn(PersonalController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(PersonalController.class).listar()).withRel("personal"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar personal activo", description = "Retorna todos los integrantes activos del equipo con enlaces HATEOAS")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<PersonalDto>> listar() {
        List<PersonalDto> lista = personalService.listarActivos();
        lista.forEach(p -> p.add(
            linkTo(methodOn(PersonalController.class).buscarPorId(p.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(PersonalController.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Buscar integrante del personal por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Personal encontrado"),
        @ApiResponse(responseCode = "404", description = "Personal no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonalDto> buscarPorId(@PathVariable Long id) {
        PersonalDto response = personalService.buscarPorId(id);
        response.add(linkTo(methodOn(PersonalController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(PersonalController.class).listar()).withRel("personal"));
        response.add(linkTo(methodOn(PersonalController.class).desactivar(id)).withRel("delete"));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filtrar personal por especialidad",
               description = "Especialidades: VETERINARIO_GENERAL, CIRUJANO, ESTILISTA_CANINO, LABORATORISTA, etc.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<PersonalDto>> porEspecialidad(
            @PathVariable Especialidad especialidad) {
        return ResponseEntity.ok(personalService.buscarPorEspecialidad(especialidad));
    }

    @Operation(summary = "Actualizar implementos asignados", description = "Body: { \"implementos\": \"Estetoscopio, guantes\" }")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Implementos actualizados correctamente"),
        @ApiResponse(responseCode = "404", description = "Personal no encontrado")
    })
    @PatchMapping("/{id}/implementos")
    public ResponseEntity<PersonalDto> actualizarImplementos(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                personalService.actualizarImplementos(id, body.get("implementos")));
    }

    @Operation(summary = "Desactivar integrante del personal (soft delete)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Personal desactivado correctamente"),
        @ApiResponse(responseCode = "404", description = "Personal no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        personalService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
