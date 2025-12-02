package com.tpi.mssolicitudes.client;

import com.tpi.mssolicitudes.dto.ContenedorCreateRequest;
import com.tpi.mssolicitudes.dto.ContenedorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CatalogoClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${catalogo.base-url}")
    private String baseUrl;

    public ContenedorDTO obtenerContenedorPorCodigo(String codigo) {
        String nonNullCodigo = Objects.requireNonNull(codigo, "El código del contenedor no puede ser nulo");
        String resolvedBaseUrl = Objects.requireNonNull(baseUrl, "La baseUrl de catálogo no puede ser nula");

        return webClientBuilder.baseUrl(resolvedBaseUrl).build()
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/contenedores/{codigo}")
                .build(nonNullCodigo))
            .retrieve()
            .bodyToMono(ContenedorDTO.class)
            .block();
    }

    public ContenedorDTO crearContenedor(ContenedorCreateRequest request) {
        ContenedorCreateRequest nonNullRequest = Objects.requireNonNull(request, "El request de contenedor no puede ser nulo");
        String resolvedBaseUrl = Objects.requireNonNull(baseUrl, "La baseUrl de catálogo no puede ser nula");

        return webClientBuilder.baseUrl(resolvedBaseUrl).build()
            .post()
            .uri("/api/contenedores")
            .body(Objects.requireNonNull(Mono.just(nonNullRequest), "El cuerpo del request no puede ser nulo"), ContenedorCreateRequest.class)
            .retrieve()
            .bodyToMono(ContenedorDTO.class)
            .block();
    }
}
