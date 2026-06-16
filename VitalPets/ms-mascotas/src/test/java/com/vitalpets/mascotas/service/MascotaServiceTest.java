package com.vitalpets.mascotas.service;

import com.vitalpets.mascotas.dto.MascotaDto;
import com.vitalpets.mascotas.model.Especie;
import com.vitalpets.mascotas.model.Mascota;
import com.vitalpets.mascotas.repository.MascotaRepository;
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
@DisplayName("Pruebas unitarias - MascotaService")
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private MascotaService mascotaService;

    private Mascota mascota;
    private MascotaDto dto;

    @BeforeEach
    void setUp() {
        mascota = Mascota.builder()
                .id(1L)
                .nombre("Firulais")
                .especie(Especie.PERRO)
                .raza("Labrador")
                .edadAnios(3)
                .sexo("MACHO")
                .pesoKg(25.0)
                .clienteId(10L)
                .activo(true)
                .build();

        dto = new MascotaDto();
        dto.setNombre("Firulais");
        dto.setEspecie(Especie.PERRO);
        dto.setRaza("Labrador");
        dto.setEdadAnios(3);
        dto.setSexo("MACHO");
        dto.setPesoKg(25.0);
        dto.setClienteId(10L);
    }

    // ─── TEST 1: Registrar mascota exitosamente ──────────────────────
    @Test
    @DisplayName("registrar() debe guardar y retornar la entidad Mascota")
    void registrar_debeGuardarYRetornarMascota() {
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascota);

        Mascota resultado = mascotaService.registrar(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Firulais");
        assertThat(resultado.getEspecie()).isEqualTo(Especie.PERRO);
        verify(mascotaRepository, times(1)).save(any(Mascota.class));
    }

    // ─── TEST 2: Listar mascotas activas ────────────────────────────
    @Test
    @DisplayName("listarActivas() debe retornar lista de mascotas activas")
    void listarActivas_debeRetornarListaConElementos() {
        when(mascotaRepository.findByActivoTrue()).thenReturn(List.of(mascota));

        List<Mascota> resultado = mascotaService.listarActivas();

        assertThat(resultado).isNotEmpty().hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Firulais");
        verify(mascotaRepository, times(1)).findByActivoTrue();
    }

    // ─── TEST 3: Listar activas devuelve lista vacía ─────────────────
    @Test
    @DisplayName("listarActivas() debe retornar lista vacía cuando no hay mascotas")
    void listarActivas_debeRetornarListaVacia() {
        when(mascotaRepository.findByActivoTrue()).thenReturn(List.of());

        List<Mascota> resultado = mascotaService.listarActivas();

        assertThat(resultado).isEmpty();
        verify(mascotaRepository, times(1)).findByActivoTrue();
    }

    // ─── TEST 4: Buscar por ID existente ─────────────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar la mascota cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarMascota() {
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        Mascota resultado = mascotaService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Firulais");
        verify(mascotaRepository, times(1)).findById(1L);
    }

    // ─── TEST 5: Buscar por ID inexistente lanza excepción ───────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando el ID no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mascotaService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");

        verify(mascotaRepository, times(1)).findById(999L);
    }

    // ─── TEST 6: Buscar por cliente ──────────────────────────────────
    @Test
    @DisplayName("buscarPorCliente() debe retornar las mascotas del cliente dado")
    void buscarPorCliente_debeRetornarMascotasDelCliente() {
        when(mascotaRepository.findByClienteId(10L)).thenReturn(List.of(mascota));

        List<Mascota> resultado = mascotaService.buscarPorCliente(10L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getClienteId()).isEqualTo(10L);
        verify(mascotaRepository, times(1)).findByClienteId(10L);
    }

    // ─── TEST 7: Buscar por especie ───────────────────────────────────
    @Test
    @DisplayName("buscarPorEspecie() debe retornar mascotas filtradas por especie")
    void buscarPorEspecie_debeRetornarMascotasDeEsaEspecie() {
        when(mascotaRepository.findByEspecie(Especie.PERRO)).thenReturn(List.of(mascota));

        List<Mascota> resultado = mascotaService.buscarPorEspecie(Especie.PERRO);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEspecie()).isEqualTo(Especie.PERRO);
        verify(mascotaRepository, times(1)).findByEspecie(Especie.PERRO);
    }

    // ─── TEST 8: Actualizar mascota existente ─────────────────────────
    @Test
    @DisplayName("actualizar() debe modificar y guardar la mascota cuando existe")
    void actualizar_cuandoExiste_debeModificarYGuardar() {
        MascotaDto dtoActualizado = new MascotaDto();
        dtoActualizado.setNombre("Firulais Jr");
        dtoActualizado.setRaza("Golden Retriever");
        dtoActualizado.setEdadAnios(4);
        dtoActualizado.setPesoKg(28.0);

        Mascota mascotaActualizada = Mascota.builder()
                .id(1L).nombre("Firulais Jr").especie(Especie.PERRO)
                .raza("Golden Retriever").edadAnios(4).pesoKg(28.0)
                .clienteId(10L).activo(true).build();

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaActualizada);

        Mascota resultado = mascotaService.actualizar(1L, dtoActualizado);

        assertThat(resultado.getNombre()).isEqualTo("Firulais Jr");
        assertThat(resultado.getEdadAnios()).isEqualTo(4);
        verify(mascotaRepository, times(1)).findById(1L);
        verify(mascotaRepository, times(1)).save(any(Mascota.class));
    }

    // ─── TEST 9: Actualizar mascota inexistente lanza excepción ──────
    @Test
    @DisplayName("actualizar() debe lanzar RuntimeException cuando el ID no existe")
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mascotaService.actualizar(999L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");

        verify(mascotaRepository, never()).save(any());
    }

    // ─── TEST 10: Desactivar mascota (soft delete) ───────────────────
    @Test
    @DisplayName("desactivar() debe setear activo=false y llamar save()")
    void desactivar_cuandoExiste_debeSetearActivoFalse() {
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascota);

        mascotaService.desactivar(1L);

        assertThat(mascota.getActivo()).isFalse();
        verify(mascotaRepository, times(1)).findById(1L);
        verify(mascotaRepository, times(1)).save(mascota);
    }

    // ─── TEST 11: Desactivar ID inexistente lanza excepción ──────────
    @Test
    @DisplayName("desactivar() debe lanzar RuntimeException cuando el ID no existe")
    void desactivar_cuandoNoExiste_debeLanzarExcepcion() {
        when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mascotaService.desactivar(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");

        verify(mascotaRepository, never()).save(any());
    }
}
