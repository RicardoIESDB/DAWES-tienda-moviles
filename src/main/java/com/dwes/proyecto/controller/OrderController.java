package com.dwes.proyecto.controller;

import com.dwes.proyecto.model.Order;
import com.dwes.proyecto.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //CONSULTAS

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> showAll() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findAll());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> showById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    // Ver pedidos de un usuario específico
    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<List<Order>> showByUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findByUserId(userId));
    }

    @GetMapping("/orders/count")
    public ResponseEntity<Map<String, Object>> count() {
        Map<String, Object> map = new HashMap<>();
        map.put("count", orderService.count());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(map);
    }

    // ***************************************************************************
    // ACTUALIZACIONES
    // ***************************************************************************

    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Order order) {
        ResponseEntity<Map<String, Object>> response;

        if (order == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo no puede estar vacío");
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            String error = "";
            if (order.getPurchaseDate() == null) {
                error += "La fecha de compra es obligatoria. ";
            }
            if (order.getUser() == null || order.getUser().getId() == null) {
                error += "El usuario es obligatorio. ";
            }
            if (order.getMobile() == null || order.getMobile().getId() == null) {
                error += "El móvil es obligatorio. ";
            }

            if (!error.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", error.trim());
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
            } else {
                try {
                    Order newOrder = orderService.save(order);
                    Map<String, Object> map = new HashMap<>();
                    map.put("mensaje", "Pedido creado con éxito");
                    map.put("insertRealizado", newOrder);
                    response = ResponseEntity.status(HttpStatus.CREATED).body(map);
                } catch (Exception e) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("error", "Error al crear pedido (verifique que el usuario y móvil existan)");
                    response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                }
            }
        }
        return response;
    }

    @PutMapping("/orders")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Order order) {
        ResponseEntity<Map<String, Object>> response;

        if (order == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo no puede estar vacío");
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            Long id = order.getId();
            Order existingObj = orderService.findById(id);

            if (existingObj == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Pedido no encontrado");
                map.put("id", id);
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {
                if (order.getPurchaseDate() != null) existingObj.setPurchaseDate(order.getPurchaseDate());
                // Permitimos cambiar usuario o móvil si se envían
                if (order.getUser() != null) existingObj.setUser(order.getUser());
                if (order.getMobile() != null) existingObj.setMobile(order.getMobile());

                Order updated = orderService.save(existingObj);
                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Pedido actualizado con éxito");
                map.put("updateRealizado", updated);
                response = ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }
        return response;
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        ResponseEntity<Map<String, Object>> response;
        Order existingObj = orderService.findById(id);

        if (existingObj == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Pedido no encontrado");
            map.put("id", id);
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {
            orderService.deleteById(id);
            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Pedido eliminado con éxito");
            map.put("deletedRealizado", existingObj);
            response = ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return response;
    }
}