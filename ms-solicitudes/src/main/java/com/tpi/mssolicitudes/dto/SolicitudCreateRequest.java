package com.tpi.mssolicitudes.dto;

import lombok.Data;

@Data
public class SolicitudCreateRequest {

    private Long clienteId;
    private ClienteCreateRequest cliente;

    // El contenedor SIEMPRE se crea junto con la solicitud.
    private ContenedorCreateRequest contenedor;

    // Origen
    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    // Destino
    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;
}
