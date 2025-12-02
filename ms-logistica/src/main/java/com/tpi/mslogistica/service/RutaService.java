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
     * Busca una ruta por su ID.
     */
    RutaDTO obtenerPorId(Long id);

    /**
     * Lista todas las rutas asociadas a una solicitud de ms-solicitudes.
     */
    List<RutaDTO> listarPorSolicitud(Long solicitudId);
    List<RutaDTO> listar();

    /**
     * Genera rutas tentativas a partir de los datos de la solicitud.
     *
     * @param request  datos de origen/destino y solicitud
     * @param cantidad cantidad sugerida de rutas (por ahora podemos usar 3 fijo)
     */
    List<RutaDTO> generarRutasTentativas(RutaCreateRequest request, int cantidad);

    /**
     * Genera rutas tentativas a partir de datos “crudos” de origen/destino.
     */
    List<RutaDTO> generarRutasTentativas(Long solicitudId,
                                         String origenDireccion,
                                         double origenLat,
                                         double origenLon,
                                         String destinoDireccion,
                                         double destinoLat,
                                         double destinoLon);
}
