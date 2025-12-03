package com.tpi.mslogistica.client;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
public class OsrmClient {

    private final RestTemplate restTemplate;

    @Value("${osrm.base-url}")
    private String baseUrl;

    public OsrmClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * Llama a OSRM para obtener la ruta real entre (origenLat, origenLon) y (destinoLat, destinoLon).
     * Devuelve distancia en metros y duración en segundos dentro de OsrmRoute.
     */
    public OsrmRoute route(double origenLat, double origenLon,
                           double destinoLat, double destinoLon) {

        String resolvedBaseUrl = Objects.requireNonNull(baseUrl, "osrm.base-url no configurada");

        // OJO: OSRM espera lon,lat (NO lat,lon)
        String url = String.format("%s/route/v1/driving/%f,%f;%f,%f?overview=false",
                resolvedBaseUrl,
                origenLon, origenLat,
                destinoLon, destinoLat
        );

        ResponseEntity<OsrmResponse> response =
                restTemplate.getForEntity(url, OsrmResponse.class);

        OsrmResponse body = response.getBody();
        if (body == null || body.routes == null || body.routes.isEmpty()) {
            throw new IllegalStateException("OSRM no devolvió rutas para " + url);
        }

        return body.routes.get(0); // nos quedamos con la mejor ruta
    }

    // ===== DTOs internos para mapear la respuesta de OSRM =====

    @Data
    public static class OsrmResponse {
        private List<OsrmRoute> routes;
        private String code;
    }

    @Data
    public static class OsrmRoute {
        private double distance; // metros
        private double duration; // segundos
    }
}
