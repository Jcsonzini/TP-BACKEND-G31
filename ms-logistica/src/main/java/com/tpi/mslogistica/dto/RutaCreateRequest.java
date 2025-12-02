package com.tpi.mslogistica.dto;

import lombok.Data;

@Data
public class RutaCreateRequest {

    // ID de la solicitud en ms-solicitudes
    private Long solicitudId;

    // Origen de la ruta (copiados de la Solicitud)
    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    // Destino de la ruta
    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;
}
