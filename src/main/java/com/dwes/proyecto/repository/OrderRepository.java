package com.dwes.proyecto.repository;

import com.dwes.proyecto.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Para ver el historial de compras de un usuario
    List<Order> findByUserId(Long userId);
}
