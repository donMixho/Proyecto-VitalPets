package com.vitalpets.laboratorio.service;

import com.vitalpets.laboratorio.client.MascotaClient;
import com.vitalpets.laboratorio.dto.ExamenDto;
import com.vitalpets.laboratorio.model.EstadoExamen;
import com.vitalpets.laboratorio.model.ExamenLaboratorio;
import com.vitalpets.laboratorio.model.TipoExamen;
import com.vitalpets.laboratorio.repository.ExamenRepository;
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
@DisplayName("Pruebas unitarias - ExamenService")
class ExamenServiceTest {

    @Mock
    private ExamenRepository examenRepository;

    @Mock
    private MascotaClient mascotaClient;

    @InjectMocks
    private ExamenService examenService;

    private ExamenLaboratorio examen;
    private ExamenDto dto;

    @BeforeEach
    void setUp() {
        examen = ExamenLaboratorio.builder()
                .id(1L).mascotaId(1L).clienteId(1L).personalId(2L)
                .tipoExamen(TipoExamen.HEMOGRAMA)
                .estado(EstadoExamen.SOLICITADO)
                .fechaSolicitud(LocalDateTime.now())
                .urgente(false).build();

        dto = new ExamenDto();
        dto.setMascotaId(1L);
        dto.setClienteId(1L);
        dto.setPersonalId(2L);
        dto.setTipoExamen(TipoExamen.HEMOGRAMA);
        dto.setUrgente(false);
    }

    // ─── TEST 1: Solicitar examen exitosamente ────────────────────────
    @Test
    @DisplayName("solicitar() debe guardar el examen cuando la mascota existe")
    void solicitar_cuandoMascotaExiste_debeGuardar() {
        when(mascotaClient.existeMascota(1L)).thenReturn(true);
        when(examenRepository.save(any(ExamenLaboratorio.class))).thenReturn(examen);

        ExamenDto resultado = examenService.solicitar(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTipoExamen()).isEqualTo(TipoExamen.HEMOGRAMA);
        assertThat(resultado.getEstado()).isEqualTo(EstadoExamen.SOLICITADO);
        verify(mascotaClient, times(1)).existeMascota(1L);
        verify(examenRepository, times(1)).save(any(ExamenLaboratorio.class));
    }

    // ─── TEST 2: Solicitar con mascota inexistente ────────────────────
    @Test
    @DisplayName("solicitar() debe lanzar excepción cuando la mascota no existe")
    void solicitar_cuandoMascotaNoExiste_debeLanzarExcepcion() {
        when(mascotaClient.existeMascota(1L)).thenReturn(false);

        assertThatThrownBy(() -> examenService.solicitar(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mascota no encontrada");

        verify(examenRepository, never()).save(any());
    }

    // ─── TEST 3: Listar todos los exámenes ───────────────────────────
    @Test
    @DisplayName("listarTodos() debe retornar todos los exámenes como DTOs")
    void listarTodos_debeRetornarTodosLosExamenes() {
        when(examenRepository.findAll()).thenReturn(List.of(examen));

        List<ExamenDto> resultado = examenService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipoExamen()).isEqualTo(TipoExamen.HEMOGRAMA);
        verify(examenRepository, times(1)).findAll();
    }

    // ─── TEST 4: Buscar examen por ID existente ───────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar el DTO cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarDto() {
        when(examenRepository.findById(1L)).thenReturn(Optional.of(examen));

        ExamenDto resultado = examenService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getMascotaId()).isEqualTo(1L);
        assertThat(resultado.getUrgente()).isFalse();
        verify(examenRepository, times(1)).findById(1L);
    }

    // ─── TEST 5: Buscar examen por ID inexistente ─────────────────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(examenRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> examenService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    // ─── TEST 6: Cargar resultado del examen ──────────────────────────
    @Test
    @DisplayName("cargarResultado() debe cambiar estado a COMPLETADO y asignar fecha resultado")
    void cargarResultado_debeSetearEstadoCompletadoYFecha() {
        ExamenLaboratorio examenCompletado = ExamenLaboratorio.builder()
                .id(1L).mascotaId(1L).tipoExamen(TipoExamen.HEMOGRAMA)
                .estado(EstadoExamen.COMPLETADO)
                .fechaSolicitud(LocalDateTime.now().minusHours(2))
                .fechaResultado(LocalDateTime.now())
                .resultados("Hemograma dentro de parámetros normales")
                .urgente(false).build();

        when(examenRepository.findById(1L)).thenReturn(Optional.of(examen));
        when(examenRepository.save(any(ExamenLaboratorio.class))).thenReturn(examenCompletado);

        ExamenDto resultado = examenService.cargarResultado(1L, "Hemograma dentro de parámetros normales", null);

        assertThat(resultado.getEstado()).isEqualTo(EstadoExamen.COMPLETADO);
        assertThat(resultado.getResultados()).isEqualTo("Hemograma dentro de parámetros normales");
        assertThat(resultado.getFechaResultado()).isNotNull();
        verify(examenRepository, times(1)).save(any(ExamenLaboratorio.class));
    }

    // ─── TEST 7: Cambiar estado del examen ───────────────────────────
    @Test
    @DisplayName("cambiarEstado() debe actualizar el estado y guardar")
    void cambiarEstado_debeActualizarEstadoYGuardar() {
        ExamenLaboratorio examenEnProceso = ExamenLaboratorio.builder()
                .id(1L).mascotaId(1L).tipoExamen(TipoExamen.HEMOGRAMA)
                .estado(EstadoExamen.EN_PROCESO).urgente(false).build();

        when(examenRepository.findById(1L)).thenReturn(Optional.of(examen));
        when(examenRepository.save(any(ExamenLaboratorio.class))).thenReturn(examenEnProceso);

        ExamenDto resultado = examenService.cambiarEstado(1L, EstadoExamen.EN_PROCESO);

        assertThat(resultado.getEstado()).isEqualTo(EstadoExamen.EN_PROCESO);
        verify(examenRepository, times(1)).save(any(ExamenLaboratorio.class));
    }

    // ─── TEST 8: Listar exámenes urgentes ────────────────────────────
    @Test
    @DisplayName("urgentes() debe retornar solo los exámenes con urgente=true")
    void urgentes_debeRetornarExamenesUrgentes() {
        ExamenLaboratorio examenUrgente = ExamenLaboratorio.builder()
                .id(2L).mascotaId(1L).tipoExamen(TipoExamen.BIOQUIMICA)
                .estado(EstadoExamen.SOLICITADO).urgente(true).build();

        when(examenRepository.findByUrgenteTrue()).thenReturn(List.of(examenUrgente));

        List<ExamenDto> resultado = examenService.urgentes();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getUrgente()).isTrue();
        verify(examenRepository, times(1)).findByUrgenteTrue();
    }
}
