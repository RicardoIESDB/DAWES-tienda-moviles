package com.dwes.proyecto.service;

import com.dwes.proyecto.model.User;
import com.dwes.proyecto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscamos el usuario en la base de datos
        User user = userRepository.findSqlByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        // Convertimos los roles booleanos a roles de Spring Security (ROLE_ADMIN, ROLE_USER)
        List<GrantedAuthority> roles = new ArrayList<>();
        if (Boolean.TRUE.equals(user.getAdministrador())) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (Boolean.TRUE.equals(user.getUsuario())) {
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        if (Boolean.TRUE.equals(user.getInvitado())) {
            roles.add(new SimpleGrantedAuthority("ROLE_GUEST"));
        }

        // Devolvemos el objeto User de Spring Security con tus datos
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Boolean.TRUE.equals(user.getActivado()), // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                roles
        );
    }
}