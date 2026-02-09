package com.dwes.proyecto.repository;

import com.dwes.proyecto.model.Mobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobileRepository extends JpaRepository<Mobile, Long> {
    // Consulta con SQL
    @Query(value = "SELECT * FROM mobiles", nativeQuery = true)
    List<Mobile> findSqlAll();

    // Consulta con SQL
    @Query(value = "SELECT * FROM mobiles WHERE id = :id", nativeQuery = true)
    Mobile findSqlById(@Param("id") Long id);

    // Consulta SQL extra: Buscar móviles de una marca concreta
    @Query(value = "SELECT * FROM mobiles WHERE brand_id = :brandId", nativeQuery = true)
    List<Mobile> findSqlByBrandId(@Param("brandId") Long brandId);

    // Consulta con SQL nativo
    @Query(value = "SELECT COUNT(*) FROM mobiles", nativeQuery = true)
    Long countSql();
}
