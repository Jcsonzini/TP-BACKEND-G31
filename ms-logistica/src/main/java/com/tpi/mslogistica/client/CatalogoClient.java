package com.tpi.mslogistica.client;

import com.tpi.mslogistica.client.dto.CamionRemotoDTO;
import com.tpi.mslogistica.client.dto.ContenedorRemotoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;


@Component
@RequiredArgsConstructor
public class CatalogoClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${ms.catalogo.base-url}")
    private String catalogoBaseUrl;

    public CamionRemotoDTO obtenerCamionPorId(Long camionId) {
        return webClientBuilder.baseUrl(catalogoBaseUrl).build()
            .get()
            .uri("/api/camiones/id/{id}", camionId)
            .retrieve()
            .bodyToMono(CamionRemotoDTO.class)
            .block();
    }

    public ContenedorRemotoDTO obtenerContenedorPorCodigo(String codigo) {
        return webClientBuilder.baseUrl(catalogoBaseUrl).build()
            .get()
            .uri("/api/contenedores/{codigo}", codigo)
            .retrieve()
            .bodyToMono(ContenedorRemotoDTO.class)
            .block();
    }

    public List<CamionRemotoDTO> obtenerTodosLosCamiones() {
        return webClientBuilder.baseUrl(catalogoBaseUrl).build()
                .get()
                .uri("/api/camiones")
                .retrieve()
                .bodyToFlux(CamionRemotoDTO.class)
                .collectList()
                .block();
    }

}
