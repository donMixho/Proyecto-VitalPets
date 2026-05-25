package com.vitalpets.historial.repository;

import com.vitalpets.historial.model.HistorialMedico;
import com.vitalpets.historial.model.TipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<HistorialMedico, Long> {
    List<HistorialMedico> findByMascotaIdOrderByFechaEventoDesc(Long mascotaId);
    List<HistorialMedico> findByClienteId(Long clienteId);
    List<HistorialMedico> findByTipoEvento(TipoEvento tipoEvento);
}