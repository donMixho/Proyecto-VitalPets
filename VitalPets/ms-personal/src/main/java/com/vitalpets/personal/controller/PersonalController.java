package com.vitalpets.personal.controller;

import com.vitalpets.personal.dto.PersonalDto;
import com.vitalpets.personal.model.Especialidad;
import com.vitalpets.personal.service.PersonalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/personal")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PersonalController {

    private final PersonalService personalService;

    @PostMapping
    public ResponseEntity<PersonalDto> registrar(@Valid @RequestBody PersonalDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personalService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<PersonalDto>> listar() {
        return ResponseEntity.ok(personalService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personalService.buscarPorId(id));
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