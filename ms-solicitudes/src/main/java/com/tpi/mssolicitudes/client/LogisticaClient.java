package com.tpi.mssolicitudes.client;

import com.tpi.mssolicitudes.client.dto.RutaCreateRequest;
import com.tpi.mssolicitudes.client.dto.RutaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LogisticaClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${logistica.base-url}")
    private String logisticaBaseUrl;

    /**
     * Llama a ms-logistica para generar rutas tentativas reales (OSRM).
     *
     * POST {baseUrl}/api/rutas/generar-tentativas?cantidad=X
     */
    public List<RutaDTO> generarRutasTentativas(RutaCreateRequest request) {

        WebClient client = webClientBuilder.baseUrl(logisticaBaseUrl).build();

        RutaDTO[] responseArray = client.post()
            .uri(uriBuilder -> uriBuilder
                .path("/api/rutas/generar-tentativas")
                .build())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(RutaDTO[].class)
            .block();

        return responseArray != null ? Arrays.asList(responseArray) : List.of();
    }

    public RutaDTO seleccionarRuta(Long rutaId) {

        WebClient client = webClientBuilder.baseUrl(logisticaBaseUrl).build();

        return client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/rutas/{rutaId}/seleccionar")
                        .build(rutaId))
                .retrieve()
                .bodyToMono(RutaDTO.class)
                .block();
    }
}
