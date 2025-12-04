package com.tpi.msgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                // Swagger / actuator libres
                .requestMatchers(
                        "/actuator/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                ).permitAll()

                // ============================
                //   ms-solicitudes (CLIENTE)
                // ============================

                // 1. Crear nueva solicitud de transporte (CLIENTE)
                .requestMatchers(HttpMethod.POST,
                        "/ms-solicitudes/api/solicitudes"
                ).hasRole("CLIENTE")

                // 2. Consultar estado del transporte de un contenedor (CLIENTE)
                .requestMatchers(HttpMethod.GET,
                        "/ms-solicitudes/api/solicitudes/seguimiento/contenedores/**"
                ).hasRole("CLIENTE")

                // ============================
                //   ms-solicitudes (OPERADOR)
                // ============================

                // 3. Consultar rutas tentativas / generar rutas (OPERADOR)
                .requestMatchers(HttpMethod.POST,
                        "/ms-solicitudes/api/solicitudes/*/generar-rutas"
                ).hasRole("OPERADOR")

                // 4. Asignar una ruta a la solicitud (OPERADOR)
                .requestMatchers(HttpMethod.POST,
                        "/ms-solicitudes/api/solicitudes/*/asignar-ruta/**"
                ).hasRole("OPERADOR")

                // 5. Consultar contenedores pendientes con filtros (OPERADOR)
                .requestMatchers(HttpMethod.GET,
                        "/ms-solicitudes/api/solicitudes/seguimiento/pendientes"
                ).hasRole("OPERADOR")

                // ============================
                //   ms-logistica (OPERADOR)
                // ============================

                // Consultar rutas y tramos (OPERADOR)
                .requestMatchers(HttpMethod.GET,
                        "/ms-logistica/api/rutas/**"
                ).hasRole("OPERADOR")

                // 6. Asignar camión a un tramo (OPERADOR)
                .requestMatchers(HttpMethod.PUT,
                        "/ms-logistica/api/tramos/*/asignar-camion"
                ).hasRole("OPERADOR")

                // ============================
                //   ms-logistica (TRANSPORTISTA)
                // ============================

                // 7. Iniciar tramo (TRANSPORTISTA)
                .requestMatchers(HttpMethod.PUT,
                        "/ms-logistica/api/tramos/*/iniciar"
                ).hasRole("TRANSPORTISTA")

                // 7. Finalizar tramo (TRANSPORTISTA)
                .requestMatchers(HttpMethod.PUT,
                        "/ms-logistica/api/tramos/*/finalizar"
                ).hasRole("TRANSPORTISTA")

                // ============================
                //   ms-catalogo (OPERADOR)
                // ============================

                // 10. Registrar y actualizar depósitos, camiones y tarifas (OPERADOR)
                .requestMatchers(
                        "/ms-catalogo/api/depositos/**",
                        "/ms-catalogo/api/camiones/**",
                        "/ms-catalogo/api/parametros-sistema/**",
                        "/ms-catalogo/api/contenedores/**"
                ).hasRole("OPERADOR")

                // Cualquier otra cosa: requiere estar autenticado
                .anyRequest().authenticated()
        );

        return http.build();
    }

    /**
     * Converter para extraer los roles de Keycloak del JWT.
     * Keycloak guarda los roles en: realm_access.roles (es un objeto anidado)
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtConverter;
    }

    /**
     * Extrae los roles del claim "realm_access.roles" de Keycloak
     */
    static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || realmAccess.isEmpty()) {
                return Collections.emptyList();
            }

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles == null) {
                return Collections.emptyList();
            }

            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        }
    }
}
