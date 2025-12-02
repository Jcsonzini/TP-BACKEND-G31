package com.tpi.mssolicitudes.client;

import com.tpi.mssolicitudes.dto.ContenedorCreateRequest;
import com.tpi.mssolicitudes.dto.ContenedorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CatalogoClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${catalogo.base-url}")
    private String baseUrl;

    public ContenedorDTO obtenerContenedorPorCodigo(String codigo) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + "/api/contenedores/" + codigo)
                .retrieve()
                .bodyToMono(ContenedorDTO.class)
                .block();
    }

    public ContenedorDTO crearContenedor(ContenedorCreateRequest request) {
        return webClientBuilder.build()
                .post()
                .uri(baseUrl + "/api/contenedores")
                .body(Mono.just(request), ContenedorCreateRequest.class)
                .retrieve()
                .bodyToMono(ContenedorDTO.class)
                .block();
    }
}
