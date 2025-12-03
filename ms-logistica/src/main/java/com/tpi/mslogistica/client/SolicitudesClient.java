package com.tpi.mslogistica.client;

import com.tpi.mslogistica.client.dto.FinalizarOperacionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class SolicitudesClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${ms.solicitudes.base-url}")
    private String msSolicitudesBaseUrl;

    public void finalizarOperacion(Long solicitudId, FinalizarOperacionRequest request) {

        WebClient client = webClientBuilder
                .baseUrl(msSolicitudesBaseUrl)
                .build();

        client.put()
                .uri("/api/solicitudes/{id}/finalizar-operacion", solicitudId)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();   // está bien bloquear acá, esto es service síncrono
    }

    public void marcarEnTransito(Long solicitudId) {

        WebClient client = webClientBuilder.baseUrl(msSolicitudesBaseUrl).build();

        client.put()
                .uri("/api/solicitudes/{id}/en-transito", solicitudId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void marcarEnDeposito(Long solicitudId) {

        WebClient client = webClientBuilder.baseUrl(msSolicitudesBaseUrl).build();

        client.put()
                .uri("/api/solicitudes/{id}/en-deposito", solicitudId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}
