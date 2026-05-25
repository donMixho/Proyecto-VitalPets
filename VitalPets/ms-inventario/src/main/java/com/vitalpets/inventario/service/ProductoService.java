package com.vitalpets.inventario.service;

import com.vitalpets.inventario.client.PersonalClient;
import com.vitalpets.inventario.dto.ProductoDto;
import com.vitalpets.inventario.model.CategoriaProducto;
import com.vitalpets.inventario.model.Producto;
import com.vitalpets.inventario.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final PersonalClient personalClient;

    public ProductoDto registrar(ProductoDto dto) {
        log.info("Registrando nuevo producto: {} - Categoría: {}",
                dto.getNombre(), dto.getCategoria());
        if (dto.getPersonalId() != null && !personalClient.existePersonal(dto.getPersonalId())) {
            log.error("No se puede registrar producto - Personal ID: {} no encontrado", dto.getPersonalId());
            throw new RuntimeException("Personal no encontrado con ID: " + dto.getPersonalId());
        }
        ProductoDto resultado = toDto(productoRepository.save(toEntity(dto)));
        log.info("Producto registrado exitosamente con ID: {}", resultado.getId());
        return resultado;
    }

    public List<ProductoDto> listarActivos() {
        log.info("Consultando listado de productos activos");
        return productoRepository.findByActivoTrue()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProductoDto buscarPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);
        return toDto(productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto no encontrado con ID: {}", id);
                    return new RuntimeException("Producto no encontrado: " + id);
                }));
    }

    public List<ProductoDto> buscarPorCategoria(CategoriaProducto categoria) {
        log.info("Buscando productos por categoría: {}", categoria);
        return productoRepository.findByCategoria(categoria)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ProductoDto> alertasStockBajo() {
        log.warn("Consultando productos con stock bajo");
        return productoRepository.findStockBajo()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProductoDto reducirStock(Long id, Integer cantidad) {
        log.info("Reduciendo stock del producto ID: {} en {} unidades", id, cantidad);
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto no encontrado para reducir stock. ID: {}", id);
                    return new RuntimeException("Producto no encontrado: " + id);
                });
        if (p.getStockActual() < cantidad) {
            log.error("Stock insuficiente para producto ID: {}. Disponible: {}, Solicitado: {}",
                    id, p.getStockActual(), cantidad);
            throw new RuntimeException("Stock insuficiente. Disponible: " + p.getStockActual());
        }
        p.setStockActual(p.getStockActual() - cantidad);
        log.info("Stock reducido correctamente. Producto ID: {} - Stock actual: {}",
                id, p.getStockActual());
        return toDto(productoRepository.save(p));
    }

    public ProductoDto aumentarStock(Long id, Integer cantidad) {
        log.info("Aumentando stock del producto ID: {} en {} unidades", id, cantidad);
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto no encontrado para aumentar stock. ID: {}", id);
                    return new RuntimeException("Producto no encontrado: " + id);
                });
        p.setStockActual(p.getStockActual() + cantidad);
        log.info("Stock aumentado correctamente. Producto ID: {} - Stock actual: {}",
                id, p.getStockActual());
        return toDto(productoRepository.save(p));
    }

    public void desactivar(Long id) {
        log.warn("Desactivando producto con ID: {}", id);
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto no encontrado para desactivar. ID: {}", id);
                    return new RuntimeException("Producto no encontrado: " + id);
                });
        p.setActivo(false);
        productoRepository.save(p);
        log.info("Producto ID: {} desactivado correctamente", id);
    }

    private ProductoDto toDto(Producto p) {
        return ProductoDto.builder()
                .id(p.getId()).nombre(p.getNombre()).descripcion(p.getDescripcion())
                .categoria(p.getCategoria()).stockActual(p.getStockActual())
                .stockMinimo(p.getStockMinimo()).precioUnitario(p.getPrecioUnitario())
                .unidadMedida(p.getUnidadMedida()).activo(p.getActivo())
                .stockBajo(p.getStockActual() <= p.getStockMinimo()).build();
    }

    private Producto toEntity(ProductoDto dto) {
        return Producto.builder()
                .nombre(dto.getNombre()).descripcion(dto.getDescripcion())
                .categoria(dto.getCategoria()).stockActual(dto.getStockActual())
                .stockMinimo(dto.getStockMinimo()).precioUnitario(dto.getPrecioUnitario())
                .unidadMedida(dto.getUnidadMedida()).build();
    }
}