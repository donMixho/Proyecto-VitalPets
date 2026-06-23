package com.vitalpets.usuarios.controller;

import com.vitalpets.usuarios.dto.UsuarioDto;
import com.vitalpets.usuarios.security.JwtUtil;
import com.vitalpets.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDto> registrar(@Valid @RequestBody UsuarioDto dto) {
        UsuarioDto response = usuarioService.registrar(dto);
        response.add(linkTo(methodOn(UsuarioController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<UsuarioDto>> listar() {
        List<UsuarioDto> lista = usuarioService.listarActivos();
        lista.forEach(u -> u.add(
            linkTo(methodOn(UsuarioController.class).buscarPorId(u.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(UsuarioController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable Long id) {
        UsuarioDto response = usuarioService.buscarPorId(id);
        response.add(linkTo(methodOn(UsuarioController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"));
        response.add(linkTo(methodOn(UsuarioController.class).desactivar(id)).withRel("delete"));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            UsuarioDto usuario = usuarioService.login(
                    body.get("username"), body.get("password"));
            String token = jwtUtil.generateToken(
                    usuario.getUsername(), usuario.getRol().name());
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("token", token);
            response.put("username", usuario.getUsername());
            response.put("rol", usuario.getRol());
            response.put("mensaje", "Login exitoso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", e.getMessage());
            error.put("status", 401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
