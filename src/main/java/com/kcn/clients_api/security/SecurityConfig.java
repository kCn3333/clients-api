package com.kcn.clients_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // BCrypt do haseł
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // JdbcUserDetailsManager z bazą danych
    @Bean
    public JdbcUserDetailsManager users() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        // opcjonalnie: jeśli tabele mają niestandardowe nazwy/kolumny
        manager.setUsersByUsernameQuery("SELECT username, password, enabled FROM app_users WHERE username = ?");
        manager.setAuthoritiesByUsernameQuery("SELECT username, authority FROM app_authorities WHERE username = ?");

        return manager;
    }

    // Główna konfiguracja Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // autoryzacja endpointów
                .authorizeHttpRequests(configurer ->
                        configurer
                                // H2 console
                                .requestMatchers(HttpMethod.GET,"/h2-console/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/h2-console/**").permitAll()
                                // Swagger i dokumentacja dostępne publicznie
                                .requestMatchers("/docs/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                                // Actuator - health i prometheus bez auth (dla Kubernetes i Prometheus)
                                .requestMatchers("/actuator/health/**").permitAll()
                                .requestMatchers("/actuator/prometheus").permitAll()

                                // USER może pobierać dane
                                .requestMatchers(HttpMethod.GET, "/api/clients").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/api/clients/**").hasRole("USER")

                                // MANAGER może dodawać i aktualizować klientów
                                .requestMatchers(HttpMethod.POST, "/api/clients").hasRole("MANAGER")
                                .requestMatchers(HttpMethod.PUT, "/api/clients/**").hasRole("MANAGER")

                                // ADMIN może usuwać klientów
                                .requestMatchers(HttpMethod.DELETE, "/api/clients/**").hasRole("ADMIN")
                )
                // wyłącz CSRF, bo REST API
                .csrf(AbstractHttpConfigurer::disable)
                // Basic Auth z własnym entry pointem (JSON zamiast popup)
                .httpBasic(http -> http.authenticationEntryPoint(authenticationEntryPoint()))
                // Obsługa dostępu zabronionego (403) w JSON
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Access denied\"}");
                        })
                );
        // h2
        httpSecurity.headers(headers->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return httpSecurity.build();
    }

    // Własny entry point dla 401 (Unauthorized) - JSON zamiast przeglądarkowego popupu
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Unauthorized access\"}");
        };
    }
}
