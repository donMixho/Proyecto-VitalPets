package com.vitalpets.facturacion.service;

import com.vitalpets.facturacion.client.CitaClient;
import com.vitalpets.facturacion.dto.FacturaDto;
import com.vitalpets.facturacion.model.EstadoFactura;
import com.vitalpets.facturacion.model.Factura;
import com.vitalpets.facturacion.model.MetodoPago;
import com.vitalpets.facturacion.repository.FacturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - FacturaService")
class FacturaServiceTest {

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private CitaClient citaClient;

    @InjectMocks
    private FacturaService facturaService;

    private Factura factura;
    private FacturaDto dto;

    @BeforeEach
    void setUp() {
        factura = Factura.builder()
                .id(1L).citaId(1L).clienteId(1L).mascotaId(1L).personalId(2L)
                .fechaEmision(LocalDateTime.now())
                .estado(EstadoFactura.PENDIENTE)
                .metodoPago(MetodoPago.PENDIENTE)
                .totalServicios(25000.0).totalProductos(5000.0).totalFinal(30000.0)
                .detalles(new ArrayList<>()).build();

        dto = new FacturaDto();
        dto.setCitaId(1L);
        dto.setClienteId(1L);
        dto.setMascotaId(1L);
        dto.setPersonalId(2L);
        dto.setDetalles(new ArrayList<>());
    }

    // ─── TEST 1: Crear factura exitosamente ───────────────────────────
    @Test
    @DisplayName("crear() debe guardar la factura cuando la cita existe")
    void crear_cuandoCitaExiste_debeGuardar() {
        when(citaClient.existeCita(1L)).thenReturn(true);
        when(facturaRepository.save(any(Factura.class))).thenReturn(factura);

        FacturaDto resultado = facturaService.crear(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo(EstadoFactura.PENDIENTE);
        assertThat(resultado.getTotalFinal()).isEqualTo(30000.0);
        verify(citaClient, times(1)).existeCita(1L);
        verify(facturaRepository, times(1)).save(any(Factura.class));
    }

    // ─── TEST 2: Crear factura sin citaId ─────────────────────────────
    @Test
    @DisplayName("crear() debe guardar sin verificar cita cuando citaId es null")
    void crear_sinCitaId_debeGuardarSinVerificar() {
        dto.setCitaId(null);
        Factura facturaSinCita = Factura.builder()
                .id(2L).clienteId(1L).mascotaId(1L).personalId(2L)
                .estado(EstadoFactura.PENDIENTE).metodoPago(MetodoPago.PENDIENTE)
                .totalServicios(0.0).totalProductos(0.0).totalFinal(0.0)
                .detalles(new ArrayList<>()).build();

        when(facturaRepository.save(any(Factura.class))).thenReturn(facturaSinCita);

        FacturaDto resultado = facturaService.crear(dto);

        assertThat(resultado).isNotNull();
        verify(citaClient, never()).existeCita(any());
        verify(facturaRepository, times(1)).save(any(Factura.class));
    }

    // ─── TEST 3: Crear con cita inexistente → excepción ──────────────
    @Test
    @DisplayName("crear() debe lanzar excepción cuando la cita no existe")
    void crear_cuandoCitaNoExiste_debeLanzarExcepcion() {
        when(citaClient.existeCita(1L)).thenReturn(false);

        assertThatThrownBy(() -> facturaService.crear(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cita no encontrada");

        verify(facturaRepository, never()).save(any());
    }

    // ─── TEST 4: Listar todas las facturas ───────────────────────────
    @Test
    @DisplayName("listarTodas() debe retornar todas las facturas como DTOs")
    void listarTodas_debeRetornarTodasLasFacturas() {
        when(facturaRepository.findAll()).thenReturn(List.of(factura));

        List<FacturaDto> resultado = facturaService.listarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTotalFinal()).isEqualTo(30000.0);
        verify(facturaRepository, times(1)).findAll();
    }

    // ─── TEST 5: Buscar factura por ID existente ──────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar el DTO cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarDto() {
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));

        FacturaDto resultado = facturaService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getMetodoPago()).isEqualTo(MetodoPago.PENDIENTE);
        verify(facturaRepository, times(1)).findById(1L);
    }

    // ─── TEST 6: Buscar por ID inexistente ───────────────────────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(facturaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facturaService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    // ─── TEST 7: Pagar factura ────────────────────────────────────────
    @Test
    @DisplayName("pagar() debe cambiar el estado a PAGADA y asignar método de pago")
    void pagar_debeActualizarEstadoYMetodoPago() {
        Factura facturaPagada = Factura.builder()
                .id(1L).estado(EstadoFactura.PAGADA)
                .metodoPago(MetodoPago.EFECTIVO).totalFinal(30000.0)
                .detalles(new ArrayList<>()).build();

        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));
        when(facturaRepository.save(any(Factura.class))).thenReturn(facturaPagada);

        FacturaDto resultado = facturaService.pagar(1L, MetodoPago.EFECTIVO);

        assertThat(resultado.getEstado()).isEqualTo(EstadoFactura.PAGADA);
        assertThat(resultado.getMetodoPago()).isEqualTo(MetodoPago.EFECTIVO);
        verify(facturaRepository, times(1)).save(any(Factura.class));
    }

    // ─── TEST 8: Anular factura ───────────────────────────────────────
    @Test
    @DisplayName("anular() debe cambiar el estado a ANULADA y guardar")
    void anular_debeSetearEstadoAnulada() {
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));
        when(facturaRepository.save(any(Factura.class))).thenReturn(factura);

        facturaService.anular(1L);

        assertThat(factura.getEstado()).isEqualTo(EstadoFactura.ANULADA);
        verify(facturaRepository, times(1)).save(factura);
    }
}
