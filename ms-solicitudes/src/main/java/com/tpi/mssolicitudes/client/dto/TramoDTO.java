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

    // Estimaciones (formateadas a 3 decimales)
    private String distanciaKmEstimada;         // ej: "45.123 km"
    private String tiempoHorasEstimada;        // ej: "0.567 h"
    private String horasEsperaDepositoEstimada;// ej: "2.345 h"

    // Valores reales (formateados)
    private String distanciaKmReal;            // ej: "45.678 km"
    private String tiempoHorasReal;            // ej: "0.789 h"
    private String horasEsperaDepositoReal;    // ej: "2.123 h"

    private String estado;
}
