package com.dwes.proyecto.repository;

import com.dwes.proyecto.model.Mobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobileRepository extends JpaRepository<Mobile, Long> {
    // Buscar todos los móviles de una marca por su ID
    List<Mobile> findByBrandId(Long brandId);
}
