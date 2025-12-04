package com.tpi.mssolicitudes.client;

import com.tpi.mssolicitudes.dto.ContenedorCreateRequest;
import com.tpi.mssolicitudes.dto.ContenedorDTO;
import com.tpi.mssolicitudes.dto.ParametrosSistemaDTO;
import com.tpi.mssolicitudes.dto.PromediosCamionesAptosDTO;
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

    public ParametrosSistemaDTO obtenerTarifaPorId(Long tarifaId) {
        Long nonNullTarifaId = Objects.requireNonNull(tarifaId, "El ID de la tarifa no puede ser nulo");
        String resolvedBaseUrl = Objects.requireNonNull(baseUrl, "La baseUrl de catálogo no puede ser nula");

        return webClientBuilder.baseUrl(resolvedBaseUrl).build()
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/parametros-sistema/{id}")
                .build(nonNullTarifaId))
            .retrieve()
            .bodyToMono(ParametrosSistemaDTO.class)
            .block();
    }

    public ParametrosSistemaDTO obtenerTarifaPorDefecto() {
        String resolvedBaseUrl = Objects.requireNonNull(baseUrl, "La baseUrl de catálogo no puede ser nula");

        return webClientBuilder.baseUrl(resolvedBaseUrl).build()
            .get()
            .uri("/api/parametros-sistema/activas")
            .retrieve()
            .bodyToFlux(ParametrosSistemaDTO.class)
            .blockFirst();
    }

    /**
     * Obtiene los promedios de consumo (litros/km) y costo base ($/km) de los camiones
     * que pueden transportar un contenedor con el peso y volumen especificados.
     * 
     * @param pesoKg peso del contenedor en kg
     * @param volumenM3 volumen del contenedor en m³
     * @return DTO con los promedios calculados
     */
    public PromediosCamionesAptosDTO obtenerPromediosCamionesAptos(Double pesoKg, Double volumenM3) {
        String resolvedBaseUrl = Objects.requireNonNull(baseUrl, "La baseUrl de catálogo no puede ser nula");
        Objects.requireNonNull(pesoKg, "El peso no puede ser nulo");
        Objects.requireNonNull(volumenM3, "El volumen no puede ser nulo");

        return webClientBuilder.baseUrl(resolvedBaseUrl).build()
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/camiones/promedios-aptos")
                .queryParam("pesoKg", pesoKg)
                .queryParam("volumenM3", volumenM3)
                .build())
            .retrieve()
            .bodyToMono(PromediosCamionesAptosDTO.class)
            .block();
    }
}
