package com.tpi.msgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        // Asociar el JWT de Keycloak con roles ROLE_*
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );

        http.authorizeHttpRequests(auth -> auth
                // Swagger / actuator libres (si querés)
                .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // ---- REGLAS POR ROL (usando paths externos del gateway) ----

                // 1) Crear solicitud (CLIENTE) -> ms-solicitudes
                .requestMatchers(HttpMethod.POST, "/ms-solicitudes/api/solicitudes/**")
                    .hasRole("CLIENTE")

                // 2) Consultar estado de transporte / contenedores (CLIENTE/OPERADOR/ADMIN)
                .requestMatchers(HttpMethod.GET, "/ms-solicitudes/api/solicitudes/seguimiento/**")
                    .hasAnyRole("CLIENTE","OPERADOR","ADMIN")

                // 3) Rutas tentativas (OPERADOR/ADMIN) -> ms-logistica
                .requestMatchers("/ms-logistica/api/rutas/tentativas/**")
                    .hasAnyRole("OPERADOR","ADMIN")

                // 4) Asignar ruta a solicitud (OPERADOR/ADMIN)
                .requestMatchers(HttpMethod.PUT, "/ms-solicitudes/api/solicitudes/*/asignar-ruta/**")
                    .hasAnyRole("OPERADOR","ADMIN")

                // 5) Contenedores pendientes / catálogo (OPERADOR/ADMIN) -> ms-solicitudes y ms-catalogo
                .requestMatchers("/ms-solicitudes/api/solicitudes/seguimiento/pendientes/**")
                    .hasAnyRole("OPERADOR","ADMIN")
                .requestMatchers("/ms-catalogo/api/contenedores/**")
                    .hasAnyRole("OPERADOR","ADMIN")
                .requestMatchers("/ms-catalogo/api/camiones/**",
                                 "/ms-catalogo/api/depositos/**",
                                 "/ms-catalogo/api/parametros-sistema/**")
                    .hasAnyRole("OPERADOR","ADMIN")

                // 6) Tramos inicio / fin (TRANSPORTISTA/ADMIN) -> ms-logistica
                .requestMatchers(HttpMethod.PUT, "/ms-logistica/api/tramos/*/iniciar")
                    .hasAnyRole("TRANSPORTISTA","ADMIN")
                .requestMatchers(HttpMethod.PUT, "/ms-logistica/api/tramos/*/finalizar")
                    .hasAnyRole("TRANSPORTISTA","ADMIN")

                // El resto: sólo autenticado
                .anyRequest().authenticated()
        );

        return http.build();
    }

    // Indicar a Spring cómo leer los roles del JWT de Keycloak
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("realm_access.roles");
        converter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtAuthConverter;
    }
}
