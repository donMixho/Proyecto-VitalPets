package com.vitalpets.vacunas.service;

import com.vitalpets.vacunas.client.MascotaClient;
import com.vitalpets.vacunas.dto.VacunaDto;
import com.vitalpets.vacunas.model.Vacuna;
import com.vitalpets.vacunas.repository.VacunaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - VacunaService")
class VacunaServiceTest {

    @Mock
    private VacunaRepository vacunaRepository;

    @Mock
    private MascotaClient mascotaClient;

    @InjectMocks
    private VacunaService vacunaService;

    private Vacuna vacuna;
    private VacunaDto dto;

    @BeforeEach
    void setUp() {
        vacuna = Vacuna.builder()
                .id(1L).mascotaId(1L).clienteId(1L).personalId(2L)
                .nombreVacuna("Parvovirus canino")
                .laboratorio("Zoetis").lote("LOT2024-001")
                .fechaAplicacion(LocalDate.now())
                .fechaProximaDosis(LocalDate.now().plusYears(1))
                .dosis("1ra dosis").observaciones("Sin reacciones")
                .vigente(true).build();

        dto = new VacunaDto();
        dto.setMascotaId(1L);
        dto.setClienteId(1L);
        dto.setPersonalId(2L);
        dto.setNombreVacuna("Parvovirus canino");
        dto.setLaboratorio("Zoetis");
        dto.setLote("LOT2024-001");
        dto.setFechaAplicacion(LocalDate.now());
        dto.setFechaProximaDosis(LocalDate.now().plusYears(1));
        dto.setDosis("1ra dosis");
    }

    // ─── TEST 1: Registrar vacuna exitosamente ────────────────────────
    @Test
    @DisplayName("registrar() debe guardar la vacuna cuando la mascota existe")
    void registrar_cuandoMascotaExiste_debeGuardar() {
        when(mascotaClient.existeMascota(1L)).thenReturn(true);
        when(vacunaRepository.save(any(Vacuna.class))).thenReturn(vacuna);

        VacunaDto resultado = vacunaService.registrar(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombreVacuna()).isEqualTo("Parvovirus canino");
        assertThat(resultado.getVigente()).isTrue();
        verify(mascotaClient, times(1)).existeMascota(1L);
        verify(vacunaRepository, times(1)).save(any(Vacuna.class));
    }

    // ─── TEST 2: Registrar con mascota inexistente ────────────────────
    @Test
    @DisplayName("registrar() debe lanzar excepción cuando la mascota no existe")
    void registrar_cuandoMascotaNoExiste_debeLanzarExcepcion() {
        when(mascotaClient.existeMascota(1L)).thenReturn(false);

        assertThatThrownBy(() -> vacunaService.registrar(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mascota no encontrada");

        verify(vacunaRepository, never()).save(any());
    }

    // ─── TEST 3: Listar vacunas vigentes ─────────────────────────────
    @Test
    @DisplayName("listarVigentes() debe retornar solo las vacunas con vigente=true")
    void listarVigentes_debeRetornarVacunasVigentes() {
        when(vacunaRepository.findByVigenteTrue()).thenReturn(List.of(vacuna));

        List<VacunaDto> resultado = vacunaService.listarVigentes();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getVigente()).isTrue();
        assertThat(resultado.get(0).getNombreVacuna()).isEqualTo("Parvovirus canino");
        verify(vacunaRepository, times(1)).findByVigenteTrue();
    }

    // ─── TEST 4: Buscar vacuna por ID existente ───────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar el DTO cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarDto() {
        when(vacunaRepository.findById(1L)).thenReturn(Optional.of(vacuna));

        VacunaDto resultado = vacunaService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getLaboratorio()).isEqualTo("Zoetis");
        assertThat(resultado.getLote()).isEqualTo("LOT2024-001");
        verify(vacunaRepository, times(1)).findById(1L);
    }

    // ─── TEST 5: Buscar vacuna por ID inexistente ─────────────────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(vacunaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vacunaService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    // ─── TEST 6: Vacunas por mascota ─────────────────────────────────
    @Test
    @DisplayName("porMascota() debe retornar las vacunas de la mascota indicada")
    void porMascota_debeRetornarVacunasDeLaMascota() {
        when(vacunaRepository.findByMascotaId(1L)).thenReturn(List.of(vacuna));

        List<VacunaDto> resultado = vacunaService.porMascota(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getMascotaId()).isEqualTo(1L);
        verify(vacunaRepository, times(1)).findByMascotaId(1L);
    }

    // ─── TEST 7: Próximas a vencer ────────────────────────────────────
    @Test
    @DisplayName("proximasAVencer() debe retornar vacunas con próxima dosis dentro de 30 días")
    void proximasAVencer_debeRetornarVacunasProximas() {
        when(vacunaRepository.findByFechaProximaDosisBeforeAndVigenteTrue(any(LocalDate.class)))
                .thenReturn(List.of(vacuna));

        List<VacunaDto> resultado = vacunaService.proximasAVencer();

        assertThat(resultado).hasSize(1);
        verify(vacunaRepository, times(1))
                .findByFechaProximaDosisBeforeAndVigenteTrue(any(LocalDate.class));
    }
}
