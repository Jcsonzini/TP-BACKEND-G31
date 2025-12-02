package com.tpi.mssolicitudes.dto;

import lombok.Data;

@Data
public class SolicitudCreateRequest {

    private Long clienteId;
    private ClienteCreateRequest cliente;

    private String contenedorCodigo;
    private ContenedorCreateRequest contenedor; // si no existe, se crea

    // Origen
    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    // Destino
    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;
}
