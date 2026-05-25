package com.vitalpets.clientes.repository;

import com.vitalpets.clientes.model.Tercero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TerceroRepository extends JpaRepository<Tercero, Long> {
    List<Tercero> findByClienteId(Long clienteId);
}