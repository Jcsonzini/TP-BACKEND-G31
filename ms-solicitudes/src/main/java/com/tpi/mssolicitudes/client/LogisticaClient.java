package com.tpi.mssolicitudes.client;

import com.tpi.mssolicitudes.client.dto.RutaCreateRequest;
import com.tpi.mssolicitudes.client.dto.RutaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
public class LogisticaClient {

    private final WebClient webClient;

    @Value("${logistica.base-url}")
    private String baseUrl;

    public LogisticaClient(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public RutaDTO crearRuta(RutaCreateRequest request) {
        RutaCreateRequest nonNullRequest = Objects.requireNonNull(request, "El request de ruta no puede ser nulo");
        String resolvedBaseUrl = Objects.requireNonNull(baseUrl, "La baseUrl de logística no puede ser nula");
        MediaType jsonMediaType = Objects.requireNonNull(MediaType.APPLICATION_JSON, "MediaType APPLICATION_JSON no puede ser nulo");

        return webClient.post()
                .uri(resolvedBaseUrl + "/api/rutas")
                .contentType(jsonMediaType)
                .bodyValue(nonNullRequest)
                .retrieve()
                .bodyToMono(RutaDTO.class)
                .block();
    }
}
