package com.vitalpets.inventario.service;

import com.vitalpets.inventario.client.PersonalClient;
import com.vitalpets.inventario.dto.ProductoDto;
import com.vitalpets.inventario.model.CategoriaProducto;
import com.vitalpets.inventario.model.Producto;
import com.vitalpets.inventario.repository.ProductoRepository;
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
@DisplayName("Pruebas unitarias - ProductoService")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private PersonalClient personalClient;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private ProductoDto dto;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1L).nombre("Amoxicilina 500mg").descripcion("Antibiótico")
                .categoria(CategoriaProducto.MEDICAMENTO)
                .stockActual(50).stockMinimo(10)
                .precioUnitario(3500.0).unidadMedida("unidad").activo(true).build();

        dto = new ProductoDto();
        dto.setNombre("Amoxicilina 500mg");
        dto.setDescripcion("Antibiótico");
        dto.setCategoria(CategoriaProducto.MEDICAMENTO);
        dto.setStockActual(50);
        dto.setStockMinimo(10);
        dto.setPrecioUnitario(3500.0);
        dto.setUnidadMedida("unidad");
    }

    // ─── TEST 1: Registrar producto sin personalId ───────────────────
    @Test
    @DisplayName("registrar() debe guardar el producto cuando personalId es null")
    void registrar_sinPersonalId_debeGuardar() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoDto resultado = productoService.registrar(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Amoxicilina 500mg");
        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(personalClient, never()).existePersonal(any());
    }

    // ─── TEST 2: Registrar con personalId válido ─────────────────────
    @Test
    @DisplayName("registrar() debe verificar personal existente antes de guardar")
    void registrar_conPersonalIdValido_debeGuardar() {
        dto.setPersonalId(5L);
        when(personalClient.existePersonal(5L)).thenReturn(true);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoDto resultado = productoService.registrar(dto);

        assertThat(resultado).isNotNull();
        verify(personalClient, times(1)).existePersonal(5L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    // ─── TEST 3: Registrar con personalId inexistente → excepción ────
    @Test
    @DisplayName("registrar() debe lanzar excepción cuando personalId no existe")
    void registrar_conPersonalIdInexistente_debeLanzarExcepcion() {
        dto.setPersonalId(99L);
        when(personalClient.existePersonal(99L)).thenReturn(false);

        assertThatThrownBy(() -> productoService.registrar(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Personal no encontrado");

        verify(productoRepository, never()).save(any());
    }

    // ─── TEST 4: Listar productos activos ────────────────────────────
    @Test
    @DisplayName("listarActivos() debe retornar lista de productos activos")
    void listarActivos_debeRetornarLista() {
        when(productoRepository.findByActivoTrue()).thenReturn(List.of(producto));

        List<ProductoDto> resultado = productoService.listarActivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Amoxicilina 500mg");
        verify(productoRepository, times(1)).findByActivoTrue();
    }

    // ─── TEST 5: Buscar por ID existente ─────────────────────────────
    @Test
    @DisplayName("buscarPorId() debe retornar el DTO cuando el ID existe")
    void buscarPorId_cuandoExiste_debeRetornarDto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        ProductoDto resultado = productoService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getCategoria()).isEqualTo(CategoriaProducto.MEDICAMENTO);
        verify(productoRepository, times(1)).findById(1L);
    }

    // ─── TEST 6: Buscar por ID inexistente ───────────────────────────
    @Test
    @DisplayName("buscarPorId() debe lanzar RuntimeException cuando no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarExcepcion() {
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    // ─── TEST 7: Reducir stock con suficiente disponible ─────────────
    @Test
    @DisplayName("reducirStock() debe descontar correctamente del stock actual")
    void reducirStock_cuandoHayStock_debeDescontar() {
        Producto productoActualizado = Producto.builder()
                .id(1L).nombre("Amoxicilina 500mg").categoria(CategoriaProducto.MEDICAMENTO)
                .stockActual(40).stockMinimo(10).precioUnitario(3500.0).activo(true).build();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);

        ProductoDto resultado = productoService.reducirStock(1L, 10);

        assertThat(resultado.getStockActual()).isEqualTo(40);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    // ─── TEST 8: Reducir stock insuficiente → excepción ──────────────
    @Test
    @DisplayName("reducirStock() debe lanzar excepción cuando el stock es insuficiente")
    void reducirStock_cuandoStockInsuficiente_debeLanzarExcepcion() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThatThrownBy(() -> productoService.reducirStock(1L, 100))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Stock insuficiente");

        verify(productoRepository, never()).save(any());
    }

    // ─── TEST 9: Aumentar stock ───────────────────────────────────────
    @Test
    @DisplayName("aumentarStock() debe sumar correctamente al stock actual")
    void aumentarStock_debeIncrementarStock() {
        Producto productoActualizado = Producto.builder()
                .id(1L).nombre("Amoxicilina 500mg").categoria(CategoriaProducto.MEDICAMENTO)
                .stockActual(70).stockMinimo(10).precioUnitario(3500.0).activo(true).build();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);

        ProductoDto resultado = productoService.aumentarStock(1L, 20);

        assertThat(resultado.getStockActual()).isEqualTo(70);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    // ─── TEST 10: Desactivar producto (soft delete) ───────────────────
    @Test
    @DisplayName("desactivar() debe setear activo=false y guardar")
    void desactivar_cuandoExiste_debeSetearActivoFalse() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        productoService.desactivar(1L);

        assertThat(producto.getActivo()).isFalse();
        verify(productoRepository, times(1)).save(producto);
    }
}
