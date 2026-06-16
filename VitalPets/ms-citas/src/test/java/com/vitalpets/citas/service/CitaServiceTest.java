package com.vitalpets.citas.service;

import com.vitalpets.citas.client.ClienteClient;
import com.vitalpets.citas.client.MascotaClient;
import com.vitalpets.citas.dto.CitaDto;
import com.vitalpets.citas.model.Cita;
import com.vitalpets.citas.model.EstadoCita;
import com.vitalpets.citas.model.TipoServicio;
import com.vitalpets.citas.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - CitaService")
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private MascotaClient mascotaClient;

    @Mock
    private ClienteClient clienteClient;

    @InjectMocks
    private CitaService citaService;

    private Cita cita;
    private CitaDto dto;

    @BeforeEach
    void setUp() {
        cita = Cita.builder()
                .id(1L).mascotaId(1L).clienteId(1L).personalId(2L)
                .tipoServicio(TipoServicio.CONSULTA_GENERAL)
                .fechaHora(LocalDateTime.now().plusDays(1))
                .estado(EstadoCita.PROGRAMADA)
                .observaciones("Sin observaciones").build();

        dto = new CitaDto();
        dto.setMascotaId(1L);
        dto.setClienteId(1L);
        dto.setPersonalId(2L);
        dto.setTipoServicio(TipoServicio.CONSULTA_GENERAL);
        dto.setFechaHora(LocalDateTime.now().plusDays(1));
        dto.setObservaciones("Sin observaciones");
    }

    // ─── TEST 1: Registrar cita exitosamente ─────────────────────────
    @Test
    @DisplayName("registrar() debe guardar la cita cuando mascota y cliente existen")
    void registrar_conDatosValidos_debeGuardar() {
        when(mascotaClient.existeMascota(1L)).thenReturn(true);
        when(clienteClient.existeCliente(1L)).thenReturn(true);
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        CitaDto resultado = citaService.registrar(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo(EstadoCita.PROGRAMADA);
        verify(mascotaClient, times(1)).existeMascota(1L);
        verify(clienteClient, times(1)).existeCliente(1L);
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    // ─── TEST 2: Registrar con mascota inexistente ────────────────────
    @Test
    @DisplayName("registrar() debe lanzar excepción cuando la mascota no existe")
    void registrar_cuandoMascotaNoExiste_debeLanzarExcepcion() {
        when(mascotaClient.existeMascota(1L)).thenReturn(false);

        assertThatThrownBy(() -> citaService.registrar(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mascota no encontrada");

        verify(citaRepository, never()).save(any());
    }

    // ─── TEST 3: Registrar con cliente inexistente ────────────────────
    @Test
    @DisplayName("registrar() debe lanzar excepción cuando el cliente no existe")
    void registrar_cuandoClienteNoExiste_debeLanzarExcepcion() {
        when(mascotaClient.existeMascota(1L)).thenReturn(true);
        when(clienteClient.existeCliente(1L)).thenReturn(false);

        assertThatThrownBy(() -> citaService.registrar(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cliente no encontrado");

        verify(citaRepository, never()).save(any());
    }

    // ─── TEST 4: Listar todas las citas ──────────────────────────────
    @Test
    @DisplayName("listarTodas() debe retornar todas las citas como DTOs")
    void listarTodas_debeRetornarTodasLasCitas() {
        when(citaRepository.findAll()).thenReturn(List.of(cita));

        List<CitaDto> resultado = citaService.listarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipoServicio()).isEqualTo(TipoServicio.CONSULTA_GENERAL);
        verify(citaRepository, times(1)).findAll();
    }

    // ─── TEST 5: Buscar cita por ID existente ────────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar el DTO cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarDto() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        CitaDto resultado = citaService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getMascotaId()).isEqualTo(1L);
        verify(citaRepository, times(1)).findById(1L);
    }

    // ─── TEST 6: Buscar cita por ID inexistente ───────────────────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(citaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> citaService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    // ─── TEST 7: Cambiar estado de la cita ───────────────────────────
    @Test
    @DisplayName("cambiarEstado() debe actualizar el estado y guardar")
    void cambiarEstado_debeActualizarEstadoYGuardar() {
        Cita citaConfirmada = Cita.builder()
                .id(1L).mascotaId(1L).clienteId(1L)
                .tipoServicio(TipoServicio.CONSULTA_GENERAL)
                .estado(EstadoCita.CONFIRMADA).build();

        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(citaConfirmada);

        CitaDto resultado = citaService.cambiarEstado(1L, EstadoCita.CONFIRMADA);

        assertThat(resultado.getEstado()).isEqualTo(EstadoCita.CONFIRMADA);
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    // ─── TEST 8: Cancelar cita ────────────────────────────────────────
    @Test
    @DisplayName("cancelar() debe cambiar el estado a CANCELADA y guardar")
    void cancelar_debeCambiarEstadoACancelada() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        citaService.cancelar(1L);

        assertThat(cita.getEstado()).isEqualTo(EstadoCita.CANCELADA);
        verify(citaRepository, times(1)).save(cita);
    }
}
