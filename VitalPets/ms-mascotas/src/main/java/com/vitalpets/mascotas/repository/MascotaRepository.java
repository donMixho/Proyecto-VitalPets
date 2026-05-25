package com.vitalpets.mascotas.repository;

import com.vitalpets.mascotas.model.Mascota;
import com.vitalpets.mascotas.model.Especie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

//findByClienteId es parte de la dependencia de "SPRING DATA JPA" y lo que hace es
//traducir los métodos de Java a consultas SQL reales ejecutadas en la base de datos
@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    // Buscar todas las mascotas de un dueño específico
    List<Mascota> findByClienteId(Long clienteId);

    // Buscar mascotas por especie (ej: todos los reptiles)
    List<Mascota> findByEspecie(Especie especie);

    // Buscar solo mascotas activas
    List<Mascota> findByActivoTrue();

    // Buscar por nombre (útil para búsqueda rápida)
    List<Mascota> findByNombreContainingIgnoreCase(String nombre);
}