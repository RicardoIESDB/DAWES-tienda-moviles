package com.dwes.proyecto.repository;

import com.dwes.proyecto.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    // Métodos HEREDADOS
    // ****************************
    /*
        findAll()
        findById(id)
        count()
        save(Entity)
        deleteById(id)
     */

    // Consulta con SQL nativo para obtener todas las marcas
    @Query(value = "SELECT * FROM brands", nativeQuery = true)
    List<Brand> findSqlAll();

    // Consulta con SQL nativo para buscar por ID
    @Query(value = "SELECT * FROM brands WHERE id = :id", nativeQuery = true)
    Brand findSqlById(@Param("id") Long id);

    // Consulta con SQL nativo para contar registros
    @Query(value = "SELECT COUNT(*) FROM brands", nativeQuery = true)
    Long countSql();
}
