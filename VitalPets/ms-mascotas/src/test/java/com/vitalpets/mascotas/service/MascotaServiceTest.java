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

// Activa Mockito para esta clase de test
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - MascotaService")
// Prueba la lógica del MascotaService sin conectarse a la base de datos
class MascotaServiceTest {

    // Repositorio falso — simula la BD sin conectarse a MySQL
    @Mock
    private MascotaRepository mascotaRepository;

    // Service real, pero con el repositorio falso inyectado
    @InjectMocks
    private MascotaService mascotaService;

    private Mascota mascota;
    private MascotaDto dto;

    // Datos de ejemplo reutilizables en todos los tests
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
        // Define qué retorna el mock cuando se llame este método
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascota);

        // Ejecuta el método real del Service
        Mascota resultado = mascotaService.registrar(dto);

        // Verifica que el resultado es el esperado
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Firulais");
        assertThat(resultado.getEspecie()).isEqualTo(Especie.PERRO);
        // Confirma que el repositorio fue llamado exactamente así
        verify(mascotaRepository, times(1)).save(any(Mascota.class));
    }

    // ─── TEST 2: Listar mascotas activas ────────────────────────────
    @Test
    @DisplayName("listarActivas() debe retornar lista de mascotas activas")
    void listarActivas_debeRetornarListaConElementos() {
        // Define qué retorna el mock cuando se llame este método
        when(mascotaRepository.findByActivoTrue()).thenReturn(List.of(mascota));

        // Ejecuta el método real del Service
        List<Mascota> resultado = mascotaService.listarActivas();

        // Verifica que el resultado es el esperado
        assertThat(resultado).isNotEmpty().hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Firulais");
        // Confirma que el repositorio fue llamado exactamente así
        verify(mascotaRepository, times(1)).findByActivoTrue();
    }

    // ─── TEST 3: Listar activas devuelve lista vacía ─────────────────
    @Test
    @DisplayName("listarActivas() debe retornar lista vacía cuando no hay mascotas")
    void listarActivas_debeRetornarListaVacia() {
        // Define qué retorna el mock cuando se llame este método
        when(mascotaRepository.findByActivoTrue()).thenReturn(List.of());

        // Ejecuta el método real del Service
        List<Mascota> resultado = mascotaService.listarActivas();

        // Verifica que el resultado es el esperado
        assertThat(resultado).isEmpty();
        // Confirma que el repositorio fue llamado exactamente así
        verify(mascotaRepository, times(1)).findByActivoTrue();
    }

    // ─── TEST 4: Buscar por ID existente ─────────────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar la mascota cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarMascota() {
        // Define qué retorna el mock cuando se llame este método
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        // Ejecuta el método real del Service
        Mascota resultado = mascotaService.buscarPorId(1L);

        // Verifica que el resultado es el esperado
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Firulais");
        // Confirma que el repositorio fue llamado exactamente así
        verify(mascotaRepository, times(1)).findById(1L);
    }

    // ─── TEST 5: Buscar por ID inexistente lanza excepción ───────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando el ID no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        // Define qué retorna el mock cuando se llame este método
        when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

        // Ejecuta el método y verifica que lanza la excepción esperada
        assertThatThrownBy(() -> mascotaService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");

        // Confirma que el repositorio fue llamado exactamente así
        verify(mascotaRepository, times(1)).findById(999L);
    }

    // ─── TEST 6: Buscar por cliente ──────────────────────────────────
    @Test
    @DisplayName("buscarPorCliente() debe retornar las mascotas del cliente dado")
    void buscarPorCliente_debeRetornarMascotasDelCliente() {
        // Define qué retorna el mock cuando se llame este método
        when(mascotaRepository.findByClienteId(10L)).thenReturn(List.of(mascota));

        // Ejecuta el método real del Service
        List<Mascota> resultado = mascotaService.buscarPorCliente(10L);

        // Verifica que el resultado es el esperado
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getClienteId()).isEqualTo(10L);
        // Confirma que el repositorio fue llamado exactamente así
        verify(mascotaRepository, times(1)).findByClienteId(10L);
    }

    // ─── TEST 7: Buscar por especie ───────────────────────────────────
    @Test
    @DisplayName("buscarPorEspecie() debe retornar mascotas filtradas por especie")
    void buscarPorEspecie_debeRetornarMascotasDeEsaEspecie() {
        // Define qué retorna el mock cuando se llame este método
        when(mascotaRepository.findByEspecie(Especie.PERRO)).thenReturn(List.of(mascota));

        // Ejecuta el método real del Service
        List<Mascota> resultado = mascotaService.buscarPorEspecie(Especie.PERRO);

        // Verifica que el resultado es el esperado
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEspecie()).isEqualTo(Especie.PERRO);
        // Confirma que el repositorio fue llamado exactamente así
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

        // Define qué retorna el mock cuando se llame este método
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaActualizada);

        // Ejecuta el método real del Service
        Mascota resultado = mascotaService.actualizar(1L, dtoActualizado);

        // Verifica que el resultado es el esperado
        assertThat(resultado.getNombre()).isEqualTo("Firulais Jr");
        assertThat(resultado.getEdadAnios()).isEqualTo(4);
        // Confirma que el repositorio fue llamado exactamente así
        verify(mascotaRepository, times(1)).findById(1L);
        verify(mascotaRepository, times(1)).save(any(Mascota.class));
    }

    // ─── TEST 9: Actualizar mascota inexistente lanza excepción ──────
    @Test
    @DisplayName("actualizar() debe lanzar RuntimeException cuando el ID no existe")
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        // Define qué retorna el mock cuando se llame este método
        when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

        // Ejecuta el método y verifica que lanza la excepción esperada
        assertThatThrownBy(() -> mascotaService.actualizar(999L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");

        // Confirma que el repositorio nunca llegó a guardar
        verify(mascotaRepository, never()).save(any());
    }

    // ─── TEST 10: Desactivar mascota (soft delete) ───────────────────
    @Test
    @DisplayName("desactivar() debe setear activo=false y llamar save()")
    void desactivar_cuandoExiste_debeSetearActivoFalse() {
        // Define qué retorna el mock cuando se llame este método
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascota);

        // Ejecuta el método real del Service
        mascotaService.desactivar(1L);

        // Verifica que el resultado es el esperado
        assertThat(mascota.getActivo()).isFalse();
        // Confirma que el repositorio fue llamado exactamente así
        verify(mascotaRepository, times(1)).findById(1L);
        verify(mascotaRepository, times(1)).save(mascota);
    }

    // ─── TEST 11: Desactivar ID inexistente lanza excepción ──────────
    @Test
    @DisplayName("desactivar() debe lanzar RuntimeException cuando el ID no existe")
    void desactivar_cuandoNoExiste_debeLanzarExcepcion() {
        // Define qué retorna el mock cuando se llame este método
        when(mascotaRepository.findById(999L)).thenReturn(Optional.empty());

        // Ejecuta el método y verifica que lanza la excepción esperada
        assertThatThrownBy(() -> mascotaService.desactivar(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");

        // Confirma que el repositorio nunca llegó a guardar
        verify(mascotaRepository, never()).save(any());
    }
}
