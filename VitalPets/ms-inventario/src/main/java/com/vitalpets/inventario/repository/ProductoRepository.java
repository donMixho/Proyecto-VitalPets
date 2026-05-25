package com.vitalpets.inventario.repository;

import com.vitalpets.inventario.model.CategoriaProducto;
import com.vitalpets.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoria(CategoriaProducto categoria);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Productos con stock por debajo del mínimo
    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo AND p.activo = true")
    List<Producto> findStockBajo();
}