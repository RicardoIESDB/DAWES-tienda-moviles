package com.dwes.proyecto.repository;

import com.dwes.proyecto.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    // Aquí podemos añadir métodos extra si necesitamos buscar por nombre, etc.
    boolean existsByName(String name);
}
