package com.tpi.mslogistica.service;

import com.tpi.mslogistica.dto.RutaCreateRequest;
import com.tpi.mslogistica.dto.RutaDTO;

import java.util.List;

public interface RutaService {

    // Crear una ruta directa ORIGEN → DESTINO (no tentativas)
    RutaDTO crearRuta(RutaCreateRequest request);

    // Obtener una ruta por id
    RutaDTO obtenerPorId(Long id);

    // Seleccionar una ruta tentativa para una solicitud y borrar las demás
    RutaDTO seleccionarRuta(Long rutaId);

    // Listar todas las rutas
    List<RutaDTO> listar();

    // Listar rutas por id de solicitud
    List<RutaDTO> listarPorSolicitud(Long solicitudId);

    // Versión "corta": generar rutas tentativas a partir de un request y una cantidad
    List<RutaDTO> generarRutasTentativas(RutaCreateRequest request, int cantidad);

    // Versión "larga": por si en algún momento se llama con parámetros sueltos
    List<RutaDTO> generarRutasTentativas(Long solicitudId,
                                         String origenDescripcion,
                                         double origenLat,
                                         double origenLon,
                                         String destinoDescripcion,
                                         double destinoLat,
                                         double destinoLon);
}
