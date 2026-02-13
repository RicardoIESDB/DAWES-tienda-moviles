package com.dwes.proyecto.config;

import com.dwes.proyecto.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
        // Debug para verificar la inyección
        System.out.println("CustomUserDetailsService inyectado: " + (customUserDetailsService != null));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/index.html",
                                        "/api-docs/**",
                                        "/v3/api-docs/**",
                                        "/webjars/swagger-ui/**").permitAll()

                                .requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers("/error").permitAll()

                                // ZONA PROTEGIDA

                                // Usuarios: Solo ADMIN
                                .requestMatchers("/api/users/**").hasRole("ADMIN")

                                // Recursos de negocio: ADMIN y USER
                                .requestMatchers("/api/mobiles/**", "/api/brands/**", "/api/orders/**").hasAnyRole("ADMIN", "USER")

                                // Consola H2: Solo ADMIN
                                .requestMatchers("/h2-console/**").hasRole("ADMIN")

                                // Resto denegado por defecto
                                .anyRequest().denyAll()
                )
                // Configuraciones adicionales para H2 y CSRF
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .disable()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame
                                .sameOrigin()
                        )
                )
                .formLogin(form ->
                        form.disable()  // Desactivamos formulario login visual
                )
                .httpBasic(httpBasic -> Customizer
                        .withDefaults()
                );
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}