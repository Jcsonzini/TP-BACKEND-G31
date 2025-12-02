package com.tpi.mslogistica.service;

import com.tpi.mslogistica.dto.RutaCreateRequest;
import com.tpi.mslogistica.dto.RutaDTO;

import java.util.List;

public interface RutaService {

    /**
     * Crea una nueva ruta logística a partir de los datos de la solicitud
     * (origen/destino, solicitudId) y calcula estimaciones básicas.
     */
    RutaDTO crearRuta(RutaCreateRequest request);

    /**
     * Obtiene una ruta por su ID.
     */
    RutaDTO obtenerPorId(Long id);

    /**
     * Lista todas las rutas.
     */
    List<RutaDTO> listar();

    /**
     * Lista todas las rutas asociadas a una solicitud de ms-solicitudes.
     */
    List<RutaDTO> listarPorSolicitud(Long solicitudId);
}
