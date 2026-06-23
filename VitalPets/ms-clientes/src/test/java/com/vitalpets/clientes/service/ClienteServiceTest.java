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

// Activa Mockito para esta clase de test
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - ClienteService")
// Prueba la lógica del ClienteService sin conectarse a la base de datos
class ClienteServiceTest {

    // Repositorio falso — simula la BD sin conectarse a MySQL
    @Mock
    private ClienteRepository clienteRepository;

    // Repositorio falso — simula la BD sin conectarse a MySQL
    @Mock
    private TerceroRepository terceroRepository;

    // Service real, pero con el repositorio falso inyectado
    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDto clienteDto;
    private Tercero tercero;
    private TerceroDto terceroDto;

    // Datos de ejemplo reutilizables en todos los tests
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
        // Define qué retorna el mock cuando se llame este método
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Ejecuta el método real del Service
        ClienteDto resultado = clienteService.registrar(clienteDto);

        // Verifica que el resultado es el esperado
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        assertThat(resultado.getApellido()).isEqualTo("Pérez");
        // Confirma que el repositorio fue llamado exactamente así
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    // ─── TEST 2: Listar clientes activos ─────────────────────────────
    @Test
    @DisplayName("listarActivos() debe retornar lista de clientes activos como DTOs")
    void listarActivos_debeRetornarListaDeDtos() {
        // Define qué retorna el mock cuando se llame este método
        when(clienteRepository.findByActivoTrue()).thenReturn(List.of(cliente));

        // Ejecuta el método real del Service
        List<ClienteDto> resultado = clienteService.listarActivos();

        // Verifica que el resultado es el esperado
        assertThat(resultado).isNotEmpty().hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Juan");
        // Confirma que el repositorio fue llamado exactamente así
        verify(clienteRepository, times(1)).findByActivoTrue();
    }

    // ─── TEST 3: Buscar cliente por ID existente ─────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar el DTO cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarDto() {
        // Define qué retorna el mock cuando se llame este método
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Ejecuta el método real del Service
        ClienteDto resultado = clienteService.buscarPorId(1L);

        // Verifica que el resultado es el esperado
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNumeroDocumento()).isEqualTo("12345678-9");
        // Confirma que el repositorio fue llamado exactamente así
        verify(clienteRepository, times(1)).findById(1L);
    }

    // ─── TEST 4: Buscar cliente por ID inexistente ───────────────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando el ID no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        // Define qué retorna el mock cuando se llame este método
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Ejecuta el método y verifica que lanza la excepción esperada
        assertThatThrownBy(() -> clienteService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");

        // Confirma que el repositorio fue llamado exactamente así
        verify(clienteRepository, times(1)).findById(999L);
    }

    // ─── TEST 5: Desactivar cliente existente (soft delete) ──────────
    @Test
    @DisplayName("desactivar() debe setear activo=false y guardar")
    void desactivar_cuandoExiste_debeSetearActivoFalse() {
        // Define qué retorna el mock cuando se llame este método
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Ejecuta el método real del Service
        clienteService.desactivar(1L);

        // Verifica que el resultado es el esperado
        assertThat(cliente.getActivo()).isFalse();
        // Confirma que el repositorio fue llamado exactamente así
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(cliente);
    }

    // ─── TEST 6: Desactivar ID inexistente lanza excepción ───────────
    @Test
    @DisplayName("desactivar() debe lanzar RuntimeException cuando el ID no existe")
    void desactivar_cuandoNoExiste_debeLanzarExcepcion() {
        // Define qué retorna el mock cuando se llame este método
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Ejecuta el método y verifica que lanza la excepción esperada
        assertThatThrownBy(() -> clienteService.desactivar(999L))
                .isInstanceOf(RuntimeException.class);

        // Confirma que el repositorio nunca llegó a guardar
        verify(clienteRepository, never()).save(any());
    }

    // ─── TEST 7: Registrar tercero autorizado ────────────────────────
    @Test
    @DisplayName("registrarTercero() debe guardar el tercero y retornar su DTO")
    void registrarTercero_debeGuardarYRetornarTerceroDto() {
        // Define qué retorna el mock cuando se llame este método
        when(terceroRepository.save(any(Tercero.class))).thenReturn(tercero);

        // Ejecuta el método real del Service
        TerceroDto resultado = clienteService.registrarTercero(terceroDto);

        // Verifica que el resultado es el esperado
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("María");
        assertThat(resultado.getClienteId()).isEqualTo(1L);
        // Confirma que el repositorio fue llamado exactamente así
        verify(terceroRepository, times(1)).save(any(Tercero.class));
    }

    // ─── TEST 8: Listar terceros por cliente ─────────────────────────
    @Test
    @DisplayName("tercerosPorCliente() debe retornar los terceros del cliente dado")
    void tercerosPorCliente_debeRetornarListaDeTerceros() {
        // Define qué retorna el mock cuando se llame este método
        when(terceroRepository.findByClienteId(1L)).thenReturn(List.of(tercero));

        // Ejecuta el método real del Service
        List<TerceroDto> resultado = clienteService.tercerosPorCliente(1L);

        // Verifica que el resultado es el esperado
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("María");
        assertThat(resultado.get(0).getRelacion()).isEqualTo("Familiar");
        // Confirma que el repositorio fue llamado exactamente así
        verify(terceroRepository, times(1)).findByClienteId(1L);
    }
}
