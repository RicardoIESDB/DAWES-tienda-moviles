package com.dwes.proyecto.service;

import com.dwes.proyecto.model.Order;
import com.dwes.proyecto.repository.MobileRepository;
import com.dwes.proyecto.repository.OrderRepository;
import com.dwes.proyecto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    public OrderRepository orderRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public MobileRepository mobileRepository;

    // ************************
    // CONSULTAS
    // ************************
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findSqlAll();
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findSqlById(id);
    }

    @Transactional(readOnly = true)
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findSqlByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return orderRepository.countSql();
    }

    // ************************
    // ACTUALIZACIONES
    // ************************

    @Transactional
    public Order save(Order order) {
        // Validamos que existan el usuario y el móvil antes de guardar
        if (order.getUser() != null && order.getUser().getId() != null) {
            User u = userRepository.findSqlById(order.getUser().getId());
            order.setUser(u); // Asignamos la entidad completa gestionada
        }
        if (order.getMobile() != null && order.getMobile().getId() != null) {
            Mobile m = mobileRepository.findSqlById(order.getMobile().getId());
            order.setMobile(m); // Asignamos la entidad completa gestionada
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Long id, Order orderDetails) {
        Order order = orderRepository.findSqlById(id);

        if (order == null) {
            throw new RuntimeException("Pedido no encontrado");
        }

        // Actualizar Fecha
        if (orderDetails.getPurchaseDate() != null) {
            order.setPurchaseDate(orderDetails.getPurchaseDate());
        }

        // Si quisieran cambiar el móvil del pedido
        if (orderDetails.getMobile() != null) {
            order.setMobile(orderDetails.getMobile());
        }

        // Si quisieran cambiar el usuario del pedido
        if (orderDetails.getUser() != null) {
            order.setUser(orderDetails.getUser());
        }

        return orderRepository.save(order);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado");
        }
        orderRepository.deleteById(id);
    }
}
