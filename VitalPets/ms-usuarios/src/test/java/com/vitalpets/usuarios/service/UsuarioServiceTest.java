package com.vitalpets.usuarios.service;

import com.vitalpets.usuarios.client.PersonalClient;
import com.vitalpets.usuarios.dto.UsuarioDto;
import com.vitalpets.usuarios.model.Rol;
import com.vitalpets.usuarios.model.Usuario;
import com.vitalpets.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - UsuarioService")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PersonalClient personalClient;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDto dto;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L).username("admin01")
                .password("clave123").nombreCompleto("Administrador General")
                .rol(Rol.ADMIN).activo(true).build();

        dto = new UsuarioDto();
        dto.setUsername("admin01");
        dto.setPassword("clave123");
        dto.setNombreCompleto("Administrador General");
        dto.setRol(Rol.ADMIN);
    }

    // ─── TEST 1: Registrar usuario sin personalId ────────────────────
    @Test
    @DisplayName("registrar() debe guardar el usuario cuando personalId es null")
    void registrar_sinPersonalId_debeGuardar() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDto resultado = usuarioService.registrar(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getUsername()).isEqualTo("admin01");
        assertThat(resultado.getRol()).isEqualTo(Rol.ADMIN);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(personalClient, never()).existePersonal(any());
    }

    // ─── TEST 2: Registrar con personalId válido ─────────────────────
    @Test
    @DisplayName("registrar() debe verificar personal existente antes de guardar")
    void registrar_conPersonalIdValido_debeGuardar() {
        dto.setPersonalId(2L);
        when(personalClient.existePersonal(2L)).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDto resultado = usuarioService.registrar(dto);

        assertThat(resultado).isNotNull();
        verify(personalClient, times(1)).existePersonal(2L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ─── TEST 3: Registrar con personalId inexistente → excepción ────
    @Test
    @DisplayName("registrar() debe lanzar excepción cuando personalId no existe")
    void registrar_conPersonalIdInexistente_debeLanzarExcepcion() {
        dto.setPersonalId(99L);
        when(personalClient.existePersonal(99L)).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.registrar(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Personal no encontrado");

        verify(usuarioRepository, never()).save(any());
    }

    // ─── TEST 4: Listar usuarios activos ─────────────────────────────
    @Test
    @DisplayName("listarActivos() debe retornar lista de usuarios activos")
    void listarActivos_debeRetornarLista() {
        when(usuarioRepository.findByActivoTrue()).thenReturn(List.of(usuario));

        List<UsuarioDto> resultado = usuarioService.listarActivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getUsername()).isEqualTo("admin01");
        verify(usuarioRepository, times(1)).findByActivoTrue();
    }

    // ─── TEST 5: Buscar por ID existente ─────────────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar el DTO cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarDto() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioDto resultado = usuarioService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getRol()).isEqualTo(Rol.ADMIN);
        verify(usuarioRepository, times(1)).findById(1L);
    }

    // ─── TEST 6: Buscar por ID inexistente ───────────────────────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    // ─── TEST 7: Login exitoso ────────────────────────────────────────
    @Test
    @DisplayName("login() debe retornar el DTO cuando usuario y contraseña son correctos")
    void login_credencialesCorrectas_debeRetornarDto() {
        when(usuarioRepository.findByUsername("admin01")).thenReturn(Optional.of(usuario));

        UsuarioDto resultado = usuarioService.login("admin01", "clave123");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsername()).isEqualTo("admin01");
        assertThat(resultado.getRol()).isEqualTo(Rol.ADMIN);
    }

    // ─── TEST 8: Login con contraseña incorrecta ──────────────────────
    @Test
    @DisplayName("login() debe lanzar excepción cuando la contraseña no coincide")
    void login_contrasenaIncorrecta_debeLanzarExcepcion() {
        when(usuarioRepository.findByUsername("admin01")).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> usuarioService.login("admin01", "claveIncorrecta"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Contraseña incorrecta");
    }

    // ─── TEST 9: Login con usuario inexistente ────────────────────────
    @Test
    @DisplayName("login() debe lanzar excepción cuando el usuario no existe")
    void login_usuarioInexistente_debeLanzarExcepcion() {
        when(usuarioRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.login("fantasma", "cualquier"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    // ─── TEST 10: Desactivar usuario (soft delete) ────────────────────
    @Test
    @DisplayName("desactivar() debe setear activo=false y guardar")
    void desactivar_cuandoExiste_debeSetearActivoFalse() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.desactivar(1L);

        assertThat(usuario.getActivo()).isFalse();
        verify(usuarioRepository, times(1)).save(usuario);
    }
}
