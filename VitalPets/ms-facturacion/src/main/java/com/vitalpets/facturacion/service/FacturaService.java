package com.vitalpets.facturacion.service;

import com.vitalpets.facturacion.dto.DetalleFacturaDto;
import com.vitalpets.facturacion.dto.FacturaDto;
import com.vitalpets.facturacion.model.*;
import com.vitalpets.facturacion.client.CitaClient;
import com.vitalpets.facturacion.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final CitaClient citaClient;

    public FacturaDto crear(FacturaDto dto) {
        log.info("Creando factura para cliente ID: {} - Mascota ID: {}",
                dto.getClienteId(), dto.getMascotaId());
        if (dto.getCitaId() != null && !citaClient.existeCita(dto.getCitaId())) {
            log.error("No se puede crear factura - Cita ID: {} no encontrada", dto.getCitaId());
            throw new RuntimeException("Cita no encontrada con ID: " + dto.getCitaId());
        }
        Factura factura = Factura.builder()
                .citaId(dto.getCitaId()).clienteId(dto.getClienteId())
                .mascotaId(dto.getMascotaId()).personalId(dto.getPersonalId())
                .nombreQuienTrae(dto.getNombreQuienTrae())
                .metodoPago(dto.getMetodoPago() != null ? dto.getMetodoPago() : MetodoPago.PENDIENTE)
                .build();

        if (dto.getDetalles() != null) {
            for (DetalleFacturaDto d : dto.getDetalles()) {
                DetalleFactura detalle = DetalleFactura.builder()
                        .factura(factura).descripcion(d.getDescripcion())
                        .tipoItem(d.getTipoItem()).cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .subtotal(d.getCantidad() * d.getPrecioUnitario()).build();
                factura.getDetalles().add(detalle);
            }
        }

        double totalServicios = factura.getDetalles().stream()
                .filter(d -> "SERVICIO".equals(d.getTipoItem()))
                .mapToDouble(DetalleFactura::getSubtotal).sum();

        double totalProductos = factura.getDetalles().stream()
                .filter(d -> "PRODUCTO".equals(d.getTipoItem()))
                .mapToDouble(DetalleFactura::getSubtotal).sum();

        factura.setTotalServicios(totalServicios);
        factura.setTotalProductos(totalProductos);
        factura.setTotalFinal(totalServicios + totalProductos);

        FacturaDto resultado = toDto(facturaRepository.save(factura));
        log.info("Factura creada exitosamente ID: {} - Total: ${}",
                resultado.getId(), resultado.getTotalFinal());
        return resultado;
}

public List<FacturaDto> listarTodas() {
        log.info("Consultando todas las facturas");
        return facturaRepository.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public FacturaDto buscarPorId(Long id) {
        log.info("Buscando factura con ID: {}", id);
        return toDto(facturaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Factura no encontrada con ID: {}", id);
                    return new RuntimeException("Factura no encontrada: " + id);
                }));
    }

    public List<FacturaDto> porCliente(Long clienteId) {
        log.info("Consultando facturas del cliente ID: {}", clienteId);
        return facturaRepository.findByClienteId(clienteId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public FacturaDto pagar(Long id, MetodoPago metodo) {
        log.info("Procesando pago de factura ID: {} - Método: {}", id, metodo);
        Factura f = facturaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Factura no encontrada para pagar. ID: {}", id);
                    return new RuntimeException("Factura no encontrada: " + id);
                });
        f.setEstado(EstadoFactura.PAGADA);
        f.setMetodoPago(metodo);
        log.info("Factura ID: {} pagada exitosamente con {}", id, metodo);
        return toDto(facturaRepository.save(f));
    }

    public void anular(Long id) {
        log.warn("Anulando factura con ID: {}", id);
        Factura f = facturaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Factura no encontrada para anular. ID: {}", id);
                    return new RuntimeException("Factura no encontrada: " + id);
                });
        f.setEstado(EstadoFactura.ANULADA);
        facturaRepository.save(f);
        log.info("Factura ID: {} anulada correctamente", id);
    }

    private FacturaDto toDto(Factura f) {
        List<DetalleFacturaDto> detalles = f.getDetalles().stream()
                .map(d -> DetalleFacturaDto.builder()
                        .id(d.getId()).descripcion(d.getDescripcion())
                        .tipoItem(d.getTipoItem()).cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario()).subtotal(d.getSubtotal()).build())
                .collect(Collectors.toList());

        return FacturaDto.builder()
                .id(f.getId()).citaId(f.getCitaId()).clienteId(f.getClienteId())
                .mascotaId(f.getMascotaId()).personalId(f.getPersonalId())
                .nombreQuienTrae(f.getNombreQuienTrae()).fechaEmision(f.getFechaEmision())
                .estado(f.getEstado()).metodoPago(f.getMetodoPago())
                .totalServicios(f.getTotalServicios()).totalProductos(f.getTotalProductos())
                .totalFinal(f.getTotalFinal()).detalles(detalles).build();
    }
}