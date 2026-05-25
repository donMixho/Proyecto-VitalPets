package com.vitalpets.usuarios.controller;

import com.vitalpets.usuarios.dto.UsuarioDto;
import com.vitalpets.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDto> registrar(@Valid @RequestBody UsuarioDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listar() {
        return ResponseEntity.ok(usuarioService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> login(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(usuarioService.login(
                body.get("username"), body.get("password")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}