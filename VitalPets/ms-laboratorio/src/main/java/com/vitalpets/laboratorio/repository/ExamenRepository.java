package com.vitalpets.laboratorio.repository;

import com.vitalpets.laboratorio.model.EstadoExamen;
import com.vitalpets.laboratorio.model.ExamenLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<ExamenLaboratorio, Long> {
    List<ExamenLaboratorio> findByMascotaId(Long mascotaId);
    List<ExamenLaboratorio> findByEstado(EstadoExamen estado);
    List<ExamenLaboratorio> findByUrgenteTrue();
}