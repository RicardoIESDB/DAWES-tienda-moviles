package com.dwes.proyecto.controller;

import com.dwes.proyecto.model.User;
import com.dwes.proyecto.service.UserService;
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
public class UserController {
    @Autowired
    private UserService userService;

    //CONSULTAS

    // GET: http://localhost:8080/mobile-store/api/users
    @GetMapping("/users")
    public ResponseEntity<List<User>> showAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    // GET: http://localhost:8080/mobile-store/api/users/1
    @GetMapping("/users/{id}")
    public ResponseEntity<User> showById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/users/count")
    public ResponseEntity<Map<String, Object>> count() {
        Map<String, Object> map = new HashMap<>();
        map.put("count", userService.count());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(map);
    }

    // ***************************************************************************
    // ACTUALIZACIONES
    // ***************************************************************************

    // POST: http://localhost:8080/mobile-store/api/users
    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody User user) {
        ResponseEntity<Map<String, Object>> response;

        if (user == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo no puede estar vacío");
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            // Validaciones manuales
            String error = "";
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                error += "El username es obligatorio. ";
            }
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                error += "El email es obligatorio. ";
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                error += "La contraseña es obligatoria. ";
            }

            if (!error.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", error.trim());
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
            } else {
                // Aseguramos valores por defecto si vienen nulos
                if (user.getAdministrador() == null) user.setAdministrador(false);
                if (user.getUsuario() == null) user.setUsuario(true);
                if (user.getInvitado() == null) user.setInvitado(false);
                if (user.getActivado() == null) user.setActivado(true);

                User newUser = userService.save(user);
                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Usuario creado con éxito");
                map.put("insertRealizado", newUser);
                response = ResponseEntity.status(HttpStatus.CREATED).body(map);
            }
        }
        return response;
    }

    // PUT: http://localhost:8080/mobile-store/api/users
    @PutMapping("/users")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody User user) {
        ResponseEntity<Map<String, Object>> response;

        if (user == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo no puede estar vacío");
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            Long id = user.getId();
            User existingObj = userService.findById(id);

            if (existingObj == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Usuario no encontrado");
                map.put("id", id);
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {
                // Actualizar campos si no son nulos
                if (user.getUsername() != null) existingObj.setUsername(user.getUsername());
                if (user.getEmail() != null) existingObj.setEmail(user.getEmail());
                if (user.getPassword() != null) existingObj.setPassword(user.getPassword());

                // Actualizar roles
                if (user.getAdministrador() != null) existingObj.setAdministrador(user.getAdministrador());
                if (user.getUsuario() != null) existingObj.setUsuario(user.getUsuario());
                if (user.getInvitado() != null) existingObj.setInvitado(user.getInvitado());
                if (user.getActivado() != null) existingObj.setActivado(user.getActivado());

                User updated = userService.save(existingObj);
                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Usuario actualizado con éxito");
                map.put("updateRealizado", updated);
                response = ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }
        return response;
    }

    // DELETE: http://localhost:8080/mobile-store/api/users/1
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        ResponseEntity<Map<String, Object>> response;
        User existingObj = userService.findById(id);

        if (existingObj == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Usuario no encontrado");
            map.put("id", id);
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {
            try {
                userService.deleteById(id);
                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Usuario eliminado con éxito");
                map.put("deletedRealizado", existingObj);
                response = ResponseEntity.status(HttpStatus.OK).body(map);
            } catch (Exception e) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "No se puede eliminar el usuario (posiblemente tiene pedidos asociados)");
                response = ResponseEntity.status(HttpStatus.CONFLICT).body(map);
            }
        }
        return response;
    }
}