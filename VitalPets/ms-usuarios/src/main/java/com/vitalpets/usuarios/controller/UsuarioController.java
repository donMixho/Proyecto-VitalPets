package com.vitalpets.usuarios.controller;

import com.vitalpets.usuarios.dto.UsuarioDto;
import com.vitalpets.usuarios.security.JwtUtil;
import com.vitalpets.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Usuarios", description = "Gestión de cuentas de acceso con roles diferenciados al sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Registrar nuevo usuario", description = "Crea una cuenta de acceso con el rol especificado")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<UsuarioDto> registrar(@Valid @RequestBody UsuarioDto dto) {
        UsuarioDto response = usuarioService.registrar(dto);
        response.add(linkTo(methodOn(UsuarioController.class).buscarPorId(response.getId())).withSelfRel());
        response.add(linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar usuarios activos", description = "Retorna todos los usuarios con activo=true (sin campo password)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<UsuarioDto>> listar() {
        List<UsuarioDto> lista = usuarioService.listarActivos();
        lista.forEach(u -> u.add(
            linkTo(methodOn(UsuarioController.class).buscarPorId(u.getId())).withSelfRel()
        ));
        return ResponseEntity.ok(CollectionModel.of(lista,
            linkTo(methodOn(UsuarioController.class).listar()).withSelfRel()));
    }

    @Operation(summary = "Buscar usuario por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable Long id) {
        UsuarioDto response = usuarioService.buscarPorId(id);
        response.add(linkTo(methodOn(UsuarioController.class).buscarPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"));
        response.add(linkTo(methodOn(UsuarioController.class).desactivar(id)).withRel("delete"));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Login de usuario",
               description = "Autentica credenciales y retorna token JWT válido por 24 horas. " +
                             "Body: { \"username\": \"admin\", \"password\": \"clave\" }")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa — retorna JWT"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
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

    @Operation(summary = "Desactivar usuario (soft delete)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuario desactivado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
