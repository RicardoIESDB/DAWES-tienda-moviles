package com.dwes.proyecto.repository;

import com.dwes.proyecto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Método derivado para buscar por nombre de usuario
    Optional<User> findByUsername(String username);

    //Método para comprobar si existe un usuario/email
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
