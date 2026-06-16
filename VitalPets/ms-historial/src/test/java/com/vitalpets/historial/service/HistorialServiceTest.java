package com.vitalpets.historial.service;

import com.vitalpets.historial.client.MascotaClient;
import com.vitalpets.historial.dto.HistorialDto;
import com.vitalpets.historial.model.HistorialMedico;
import com.vitalpets.historial.model.TipoEvento;
import com.vitalpets.historial.repository.HistorialRepository;
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
@DisplayName("Pruebas unitarias - HistorialService")
class HistorialServiceTest {

    @Mock
    private HistorialRepository historialRepository;

    @Mock
    private MascotaClient mascotaClient;

    @InjectMocks
    private HistorialService historialService;

    private HistorialMedico historial;
    private HistorialDto dto;

    @BeforeEach
    void setUp() {
        historial = HistorialMedico.builder()
                .id(1L).mascotaId(1L).clienteId(1L).personalId(2L)
                .tipoEvento(TipoEvento.CONSULTA)
                .fechaEvento(LocalDateTime.now())
                .descripcion("Control de rutina anual")
                .diagnostico("Estado saludable")
                .tratamiento("Ninguno").build();

        dto = new HistorialDto();
        dto.setMascotaId(1L);
        dto.setClienteId(1L);
        dto.setPersonalId(2L);
        dto.setTipoEvento(TipoEvento.CONSULTA);
        dto.setFechaEvento(LocalDateTime.now());
        dto.setDescripcion("Control de rutina anual");
        dto.setDiagnostico("Estado saludable");
        dto.setTratamiento("Ninguno");
    }

    // ─── TEST 1: Registrar historial exitosamente ─────────────────────
    @Test
    @DisplayName("registrar() debe guardar el registro cuando la mascota existe")
    void registrar_cuandoMascotaExiste_debeGuardar() {
        when(mascotaClient.existeMascota(1L)).thenReturn(true);
        when(historialRepository.save(any(HistorialMedico.class))).thenReturn(historial);

        HistorialDto resultado = historialService.registrar(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTipoEvento()).isEqualTo(TipoEvento.CONSULTA);
        verify(mascotaClient, times(1)).existeMascota(1L);
        verify(historialRepository, times(1)).save(any(HistorialMedico.class));
    }

    // ─── TEST 2: Registrar con mascota inexistente ────────────────────
    @Test
    @DisplayName("registrar() debe lanzar excepción cuando la mascota no existe")
    void registrar_cuandoMascotaNoExiste_debeLanzarExcepcion() {
        when(mascotaClient.existeMascota(1L)).thenReturn(false);

        assertThatThrownBy(() -> historialService.registrar(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mascota no encontrada");

        verify(historialRepository, never()).save(any());
    }

    // ─── TEST 3: Listar todos los registros ──────────────────────────
    @Test
    @DisplayName("listarTodos() debe retornar todos los registros de historial")
    void listarTodos_debeRetornarTodosLosRegistros() {
        when(historialRepository.findAll()).thenReturn(List.of(historial));

        List<HistorialDto> resultado = historialService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getDiagnostico()).isEqualTo("Estado saludable");
        verify(historialRepository, times(1)).findAll();
    }

    // ─── TEST 4: Buscar historial por ID existente ────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar el DTO cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarDto() {
        when(historialRepository.findById(1L)).thenReturn(Optional.of(historial));

        HistorialDto resultado = historialService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getMascotaId()).isEqualTo(1L);
        assertThat(resultado.getTratamiento()).isEqualTo("Ninguno");
        verify(historialRepository, times(1)).findById(1L);
    }

    // ─── TEST 5: Buscar historial por ID inexistente ──────────────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(historialRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> historialService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    // ─── TEST 6: Historial por mascota ───────────────────────────────
    @Test
    @DisplayName("porMascota() debe retornar registros ordenados por fecha para la mascota")
    void porMascota_debeRetornarHistorialDeLaMascota() {
        when(historialRepository.findByMascotaIdOrderByFechaEventoDesc(1L))
                .thenReturn(List.of(historial));

        List<HistorialDto> resultado = historialService.porMascota(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getMascotaId()).isEqualTo(1L);
        verify(historialRepository, times(1)).findByMascotaIdOrderByFechaEventoDesc(1L);
    }

    // ─── TEST 7: Historial por cliente ───────────────────────────────
    @Test
    @DisplayName("porCliente() debe retornar todos los registros del cliente")
    void porCliente_debeRetornarHistorialDelCliente() {
        when(historialRepository.findByClienteId(1L)).thenReturn(List.of(historial));

        List<HistorialDto> resultado = historialService.porCliente(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getClienteId()).isEqualTo(1L);
        verify(historialRepository, times(1)).findByClienteId(1L);
    }
}
