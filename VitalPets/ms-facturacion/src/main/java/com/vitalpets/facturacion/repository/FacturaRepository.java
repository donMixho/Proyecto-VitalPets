package com.vitalpets.facturacion.repository;

import com.vitalpets.facturacion.model.EstadoFactura;
import com.vitalpets.facturacion.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByClienteId(Long clienteId);
    List<Factura> findByEstado(EstadoFactura estado);
    List<Factura> findByMascotaId(Long mascotaId);
}