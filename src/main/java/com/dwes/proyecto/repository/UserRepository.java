package com.dwes.proyecto.repository;

import com.dwes.proyecto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Metodo derivado de JPA para buscar por nombre de usuario
    Optional<User> findByUsername(String username);

    //Metodo para comprobar si existe un usuario/email
    // boolean existsByUsername(String username);
    // boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM users_security", nativeQuery = true)
    List<User> findSqlAll();

    @Query(value = "SELECT * FROM users_security WHERE id = :id", nativeQuery = true)
    User findSqlById(@Param("id") Long id);

    // SQL nativo para buscar por nombre (útil para validaciones)
    @Query(value = "SELECT * FROM users_security WHERE username = :username", nativeQuery = true)
    User findSqlByUsername(@Param("username") String username);

    @Query(value = "SELECT COUNT(*) FROM users_security", nativeQuery = true)
    Long countSql();
}
