package com.vitalpets.vacunas.repository;

import com.vitalpets.vacunas.model.Vacuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VacunaRepository extends JpaRepository<Vacuna, Long> {
    List<Vacuna> findByMascotaId(Long mascotaId);
    List<Vacuna> findByVigenteTrue();
    List<Vacuna> findByFechaProximaDosisBeforeAndVigenteTrue(LocalDate fecha);
}