package com.vitalpets.personal.repository;

import com.vitalpets.personal.model.Personal;
import com.vitalpets.personal.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PersonalRepository extends JpaRepository<Personal, Long> {
    List<Personal> findByActivoTrue();
    List<Personal> findByEspecialidad(Especialidad especialidad);
    List<Personal> findByNombreContainingIgnoreCase(String nombre);
}