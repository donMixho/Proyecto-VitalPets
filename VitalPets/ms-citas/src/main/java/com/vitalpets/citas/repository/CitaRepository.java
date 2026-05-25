package com.vitalpets.citas.repository;

import com.vitalpets.citas.model.Cita;
import com.vitalpets.citas.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByClienteId(Long clienteId);
    List<Cita> findByMascotaId(Long mascotaId);
    List<Cita> findByPersonalId(Long personalId);
    List<Cita> findByEstado(EstadoCita estado);
}