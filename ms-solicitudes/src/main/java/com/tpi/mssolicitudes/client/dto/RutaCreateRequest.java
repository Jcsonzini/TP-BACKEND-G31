package com.tpi.mssolicitudes.client.dto;

import lombok.Data;

@Data
public class RutaCreateRequest {

    private Long solicitudId;

    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;
}
