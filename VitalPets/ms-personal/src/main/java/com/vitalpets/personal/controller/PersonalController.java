package com.vitalpets.personal.controller;

import com.vitalpets.personal.dto.PersonalDto;
import com.vitalpets.personal.model.Especialidad;
import com.vitalpets.personal.service.PersonalService;
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
public class PersonalController {

    private final PersonalService personalService;

    @PostMapping
    public ResponseEntity<PersonalDto> registrar(@Valid @RequestBody PersonalDto dto) {
        PersonalDto response = personalService.registrar(dto);
        response.add(linkTo(methodOn(PersonalController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(PersonalController.class).listar()).withRel("personal"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<PersonalDto>> listar() {
        List<PersonalDto> lista = personalService.listarActivos();
        lista.forEach(p -> p.add(
            linkTo(methodOn(PersonalController.class).buscarPorId(p.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(PersonalController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalDto> buscarPorId(@PathVariable Long id) {
        PersonalDto response = personalService.buscarPorId(id);
        response.add(linkTo(methodOn(PersonalController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(PersonalController.class).listar()).withRel("personal"));
        response.add(linkTo(methodOn(PersonalController.class).desactivar(id)).withRel("delete"));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<PersonalDto>> porEspecialidad(
            @PathVariable Especialidad especialidad) {
        return ResponseEntity.ok(personalService.buscarPorEspecialidad(especialidad));
    }

    @PatchMapping("/{id}/implementos")
    public ResponseEntity<PersonalDto> actualizarImplementos(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                personalService.actualizarImplementos(id, body.get("implementos")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        personalService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
