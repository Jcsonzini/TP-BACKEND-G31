package com.tpi.mslogistica.service;

import com.tpi.mslogistica.client.CatalogoClient;
import com.tpi.mslogistica.client.dto.CamionRemotoDTO;
import com.tpi.mslogistica.domain.EstadoTramo;
import com.tpi.mslogistica.repository.TramoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CamionAvailabilityService {

    private final CatalogoClient catalogoClient;
    private final TramoRepository tramoRepository;

    public List<CamionRemotoDTO> obtenerCamionesOcupados() {
        List<CamionRemotoDTO> todos = catalogoClient.obtenerTodosLosCamiones();

        return todos.stream()
                .filter(camion ->
                        tramoRepository.existsByCamionIdAndEstado(
                                camion.getId(),
                                EstadoTramo.EN_CURSO
                        )
                )
                .toList();
    }

    public List<CamionRemotoDTO> obtenerCamionesLibres() {
        List<CamionRemotoDTO> todos = catalogoClient.obtenerTodosLosCamiones();

        return todos.stream()
                .filter(camion ->
                        !tramoRepository.existsByCamionIdAndEstado(
                                camion.getId(),
                                EstadoTramo.EN_CURSO
                        )
                )
                .toList();
    }
}
