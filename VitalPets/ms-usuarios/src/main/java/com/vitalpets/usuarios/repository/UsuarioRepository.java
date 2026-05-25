package com.vitalpets.usuarios.repository;

import com.vitalpets.usuarios.model.Usuario;
import com.vitalpets.usuarios.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    List<Usuario> findByRol(Rol rol);
    List<Usuario> findByActivoTrue();
}