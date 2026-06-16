package com.vitalpets.personal.service;

import com.vitalpets.personal.client.UsuarioClient;
import com.vitalpets.personal.dto.PersonalDto;
import com.vitalpets.personal.model.Especialidad;
import com.vitalpets.personal.model.Personal;
import com.vitalpets.personal.repository.PersonalRepository;
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
@DisplayName("Pruebas unitarias - PersonalService")
class PersonalServiceTest {

    @Mock
    private PersonalRepository personalRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private PersonalService personalService;

    private Personal personal;
    private PersonalDto dto;

    @BeforeEach
    void setUp() {
        personal = Personal.builder()
                .id(1L).nombre("Dra. Valeria").apellido("Torres")
                .rut("15678901-2").telefono("+56911111111")
                .email("valeria@vitalpets.cl").profesion("Médico Veterinario")
                .especialidad(Especialidad.VETERINARIO_GENERAL)
                .implementosAsignados("Estetoscopio, guantes").activo(true).build();

        dto = new PersonalDto();
        dto.setNombre("Dra. Valeria");
        dto.setApellido("Torres");
        dto.setRut("15678901-2");
        dto.setTelefono("+56911111111");
        dto.setEmail("valeria@vitalpets.cl");
        dto.setProfesion("Médico Veterinario");
        dto.setEspecialidad(Especialidad.VETERINARIO_GENERAL);
    }

    // ─── TEST 1: Registrar personal sin usuarioId ────────────────────
    @Test
    @DisplayName("registrar() debe guardar el personal cuando usuarioId es null")
    void registrar_sinUsuarioId_debeGuardar() {
        when(personalRepository.save(any(Personal.class))).thenReturn(personal);

        PersonalDto resultado = personalService.registrar(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Dra. Valeria");
        verify(personalRepository, times(1)).save(any(Personal.class));
        verify(usuarioClient, never()).existeUsuario(any());
    }

    // ─── TEST 2: Registrar con usuarioId válido ──────────────────────
    @Test
    @DisplayName("registrar() debe verificar usuario existente antes de guardar")
    void registrar_conUsuarioIdValido_debeGuardar() {
        dto.setUsuarioId(3L);
        when(usuarioClient.existeUsuario(3L)).thenReturn(true);
        when(personalRepository.save(any(Personal.class))).thenReturn(personal);

        PersonalDto resultado = personalService.registrar(dto);

        assertThat(resultado).isNotNull();
        verify(usuarioClient, times(1)).existeUsuario(3L);
        verify(personalRepository, times(1)).save(any(Personal.class));
    }

    // ─── TEST 3: Registrar con usuarioId inexistente → excepción ─────
    @Test
    @DisplayName("registrar() debe lanzar excepción cuando usuarioId no existe")
    void registrar_conUsuarioIdInexistente_debeLanzarExcepcion() {
        dto.setUsuarioId(99L);
        when(usuarioClient.existeUsuario(99L)).thenReturn(false);

        assertThatThrownBy(() -> personalService.registrar(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(personalRepository, never()).save(any());
    }

    // ─── TEST 4: Listar personal activo ──────────────────────────────
    @Test
    @DisplayName("listarActivos() debe retornar lista del personal activo")
    void listarActivos_debeRetornarLista() {
        when(personalRepository.findByActivoTrue()).thenReturn(List.of(personal));

        List<PersonalDto> resultado = personalService.listarActivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEspecialidad()).isEqualTo(Especialidad.VETERINARIO_GENERAL);
        verify(personalRepository, times(1)).findByActivoTrue();
    }

    // ─── TEST 5: Buscar por ID existente ─────────────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar el DTO cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarDto() {
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));

        PersonalDto resultado = personalService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getProfesion()).isEqualTo("Médico Veterinario");
        verify(personalRepository, times(1)).findById(1L);
    }

    // ─── TEST 6: Buscar por ID inexistente ───────────────────────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(personalRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    // ─── TEST 7: Buscar por especialidad ─────────────────────────────
    @Test
    @DisplayName("buscarPorEspecialidad() debe filtrar por la especialidad indicada")
    void buscarPorEspecialidad_debeRetornarFiltrado() {
        when(personalRepository.findByEspecialidad(Especialidad.VETERINARIO_GENERAL))
                .thenReturn(List.of(personal));

        List<PersonalDto> resultado = personalService.buscarPorEspecialidad(Especialidad.VETERINARIO_GENERAL);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEspecialidad()).isEqualTo(Especialidad.VETERINARIO_GENERAL);
    }

    // ─── TEST 8: Actualizar implementos ──────────────────────────────
    @Test
    @DisplayName("actualizarImplementos() debe actualizar y guardar los implementos")
    void actualizarImplementos_debeActualizarYGuardar() {
        Personal personalActualizado = Personal.builder()
                .id(1L).nombre("Dra. Valeria").apellido("Torres")
                .especialidad(Especialidad.VETERINARIO_GENERAL)
                .implementosAsignados("Nuevo implemento").activo(true).build();

        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(personalRepository.save(any(Personal.class))).thenReturn(personalActualizado);

        PersonalDto resultado = personalService.actualizarImplementos(1L, "Nuevo implemento");

        assertThat(resultado.getImplementosAsignados()).isEqualTo("Nuevo implemento");
        verify(personalRepository, times(1)).save(any(Personal.class));
    }

    // ─── TEST 9: Desactivar personal (soft delete) ───────────────────
    @Test
    @DisplayName("desactivar() debe setear activo=false y guardar")
    void desactivar_cuandoExiste_debeSetearActivoFalse() {
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(personalRepository.save(any(Personal.class))).thenReturn(personal);

        personalService.desactivar(1L);

        assertThat(personal.getActivo()).isFalse();
        verify(personalRepository, times(1)).save(personal);
    }
}
