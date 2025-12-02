package com.tpi.mssolicitudes.client.dto;

import lombok.Data;

@Data
public class TramoDTO {

    private Long id;
    private Integer orden;
    private String origenDescripcion;
    private Double origenLatitud;
    private Double origenLongitud;
    private String destinoDescripcion;
    private Double destinoLatitud;
    private Double destinoLongitud;

    private Double distanciaKmEstimada;
    private Double tiempoHorasEstimada;
    private Double horasEsperaDepositoEstimada;

    private Double distanciaKmReal;
    private Double tiempoHorasReal;
    private Double horasEsperaDepositoReal;

    private String estado;
}
