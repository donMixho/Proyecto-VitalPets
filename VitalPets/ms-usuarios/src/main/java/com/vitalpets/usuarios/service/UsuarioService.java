package com.vitalpets.usuarios.service;

import com.vitalpets.usuarios.client.PersonalClient;
import com.vitalpets.usuarios.dto.UsuarioDto;
import com.vitalpets.usuarios.model.Usuario;
import com.vitalpets.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonalClient personalClient;

    public UsuarioDto registrar(UsuarioDto dto) {
        log.info("Registrando nuevo usuario: {} - Rol: {}",
                dto.getUsername(), dto.getRol());
        if (dto.getPersonalId() != null && !personalClient.existePersonal(dto.getPersonalId())) {
            log.error("No se puede registrar usuario - Personal ID: {} no encontrado", dto.getPersonalId());
            throw new RuntimeException("Personal no encontrado con ID: " + dto.getPersonalId());
        }
        UsuarioDto resultado = toDto(usuarioRepository.save(toEntity(dto)));
        log.info("Usuario registrado exitosamente con ID: {}", resultado.getId());
        return resultado;
    }

    public List<UsuarioDto> listarActivos() {
        log.info("Consultando listado de usuarios activos");
        return usuarioRepository.findByActivoTrue()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public UsuarioDto buscarPorId(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        return toDto(usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado: " + id);
                }));
    }

    public UsuarioDto login(String username, String password) {
        log.info("Intento de login para usuario: {}", username);
        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Login fallido - Usuario no existe: {}", username);
                    return new RuntimeException("Usuario no encontrado");
                });
        if (!u.getPassword().equals(password)) {
            log.error("Login fallido - Contraseña incorrecta para usuario: {}", username);
            throw new RuntimeException("Contraseña incorrecta");
        }
        log.info("Login exitoso para usuario: {} - Rol: {}", username, u.getRol());
        return toDto(u);
    }

    public void desactivar(Long id) {
        log.warn("Desactivando usuario con ID: {}", id);
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado para desactivar. ID: {}", id);
                    return new RuntimeException("Usuario no encontrado: " + id);
                });
        u.setActivo(false);
        usuarioRepository.save(u);
        log.info("Usuario ID: {} desactivado correctamente", id);
    }

    private UsuarioDto toDto(Usuario u) {
        return UsuarioDto.builder()
                .id(u.getId()).username(u.getUsername())
                .nombreCompleto(u.getNombreCompleto())
                .rol(u.getRol()).activo(u.getActivo()).build();
    }

    private Usuario toEntity(UsuarioDto dto) {
        return Usuario.builder()
                .username(dto.getUsername()).password(dto.getPassword())
                .nombreCompleto(dto.getNombreCompleto()).rol(dto.getRol()).build();
    }
}