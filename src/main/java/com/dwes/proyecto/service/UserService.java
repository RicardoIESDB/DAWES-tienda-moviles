package com.dwes.proyecto.service;

import com.dwes.proyecto.model.User;
import com.dwes.proyecto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    public UserRepository userRepository;

    // ************************
    // CONSULTAS
    // ************************
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findSqlAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findSqlById(id);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findSqlByUsername(username);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return userRepository.countSql();
    }

    // ************************
    // ACTUALIZACIONES
    // ************************

    @Transactional
    public User save(User user) {
        // En un futuro aquí encriptaríamos la contraseña con BCrypt
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, User userDetails) {
        User user = userRepository.findSqlById(id);

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Actualizamos campos básicos
        if (userDetails.getUsername() != null) {
            user.setUsername(userDetails.getUsername());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPassword() != null) {
            user.setPassword(userDetails.getPassword());
        }

        // Actualizamos roles
        if (userDetails.getAdministrador() != null) {
            user.setAdministrador(userDetails.getAdministrador());
        }
        if (userDetails.getUsuario() != null) {
            user.setUsuario(userDetails.getUsuario());
        }
        if (userDetails.getInvitado() != null) {
            user.setInvitado(userDetails.getInvitado());
        }
        if (userDetails.getActivado() != null) {
            user.setActivado(userDetails.getActivado());
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }
}
