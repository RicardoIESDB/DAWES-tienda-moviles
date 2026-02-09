package com.dwes.proyecto.repository;

import com.dwes.proyecto.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders", nativeQuery = true)
    List<Order> findSqlAll();

    @Query(value = "SELECT * FROM orders WHERE id = :id", nativeQuery = true)
    Order findSqlById(@Param("id") Long id);

    // SQL nativo para ver pedidos de un usuario
    @Query(value = "SELECT * FROM orders WHERE user_id = :userId", nativeQuery = true)
    List<Order> findSqlByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) FROM orders", nativeQuery = true)
    Long countSql();
}