package com.vitalpets.clientes.service;

import com.vitalpets.clientes.dto.ClienteDto;
import com.vitalpets.clientes.dto.TerceroDto;
import com.vitalpets.clientes.model.Cliente;
import com.vitalpets.clientes.model.Tercero;
import com.vitalpets.clientes.model.TipoDocumento;
import com.vitalpets.clientes.repository.ClienteRepository;
import com.vitalpets.clientes.repository.TerceroRepository;
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
@DisplayName("Pruebas unitarias - ClienteService")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private TerceroRepository terceroRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDto clienteDto;
    private Tercero tercero;
    private TerceroDto terceroDto;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1L).nombre("Juan").apellido("Pérez")
                .tipoDocumento(TipoDocumento.RUT).numeroDocumento("12345678-9")
                .telefono("+56912345678").email("juan@email.com")
                .direccion("Av. Siempreviva 742").activo(true).build();

        clienteDto = new ClienteDto();
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Pérez");
        clienteDto.setTipoDocumento(TipoDocumento.RUT);
        clienteDto.setNumeroDocumento("12345678-9");
        clienteDto.setTelefono("+56912345678");
        clienteDto.setEmail("juan@email.com");

        tercero = Tercero.builder()
                .id(1L).nombre("María").apellido("López")
                .telefono("+56987654321").clienteId(1L)
                .relacion("Familiar").activo(true).build();

        terceroDto = new TerceroDto();
        terceroDto.setNombre("María");
        terceroDto.setApellido("López");
        terceroDto.setTelefono("+56987654321");
        terceroDto.setClienteId(1L);
        terceroDto.setRelacion("Familiar");
    }

    // ─── TEST 1: Registrar cliente ───────────────────────────────────
    @Test
    @DisplayName("registrar() debe guardar el cliente y retornar su DTO")
    void registrar_debeGuardarYRetornarClienteDto() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteDto resultado = clienteService.registrar(clienteDto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        assertThat(resultado.getApellido()).isEqualTo("Pérez");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    // ─── TEST 2: Listar clientes activos ─────────────────────────────
    @Test
    @DisplayName("listarActivos() debe retornar lista de clientes activos como DTOs")
    void listarActivos_debeRetornarListaDeDtos() {
        when(clienteRepository.findByActivoTrue()).thenReturn(List.of(cliente));

        List<ClienteDto> resultado = clienteService.listarActivos();

        assertThat(resultado).isNotEmpty().hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Juan");
        verify(clienteRepository, times(1)).findByActivoTrue();
    }

    // ─── TEST 3: Buscar cliente por ID existente ─────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar el DTO cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarDto() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        ClienteDto resultado = clienteService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNumeroDocumento()).isEqualTo("12345678-9");
        verify(clienteRepository, times(1)).findById(1L);
    }

    // ─── TEST 4: Buscar cliente por ID inexistente ───────────────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando el ID no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");

        verify(clienteRepository, times(1)).findById(999L);
    }

    // ─── TEST 5: Desactivar cliente existente (soft delete) ──────────
    @Test
    @DisplayName("desactivar() debe setear activo=false y guardar")
    void desactivar_cuandoExiste_debeSetearActivoFalse() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        clienteService.desactivar(1L);

        assertThat(cliente.getActivo()).isFalse();
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(cliente);
    }

    // ─── TEST 6: Desactivar ID inexistente lanza excepción ───────────
    @Test
    @DisplayName("desactivar() debe lanzar RuntimeException cuando el ID no existe")
    void desactivar_cuandoNoExiste_debeLanzarExcepcion() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.desactivar(999L))
                .isInstanceOf(RuntimeException.class);

        verify(clienteRepository, never()).save(any());
    }

    // ─── TEST 7: Registrar tercero autorizado ────────────────────────
    @Test
    @DisplayName("registrarTercero() debe guardar el tercero y retornar su DTO")
    void registrarTercero_debeGuardarYRetornarTerceroDto() {
        when(terceroRepository.save(any(Tercero.class))).thenReturn(tercero);

        TerceroDto resultado = clienteService.registrarTercero(terceroDto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("María");
        assertThat(resultado.getClienteId()).isEqualTo(1L);
        verify(terceroRepository, times(1)).save(any(Tercero.class));
    }

    // ─── TEST 8: Listar terceros por cliente ─────────────────────────
    @Test
    @DisplayName("tercerosPorCliente() debe retornar los terceros del cliente dado")
    void tercerosPorCliente_debeRetornarListaDeTerceros() {
        when(terceroRepository.findByClienteId(1L)).thenReturn(List.of(tercero));

        List<TerceroDto> resultado = clienteService.tercerosPorCliente(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("María");
        assertThat(resultado.get(0).getRelacion()).isEqualTo("Familiar");
        verify(terceroRepository, times(1)).findByClienteId(1L);
    }
}
