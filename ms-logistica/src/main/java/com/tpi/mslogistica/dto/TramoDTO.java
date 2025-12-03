package com.tpi.mslogistica.dto;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class TramoDTO {

    private Long id;
    private Integer orden;
    private String tipo;  // ORIGEN, INTERMEDIO, DESTINO

    private String origenDescripcion;
    private Double origenLatitud;
    private Double origenLongitud;

    private String destinoDescripcion;
    private Double destinoLatitud;
    private Double destinoLongitud;

    private String distanciaKmEstimada;      // ej: "45.123 km"
    private String tiempoHorasEstimada;      // ej: "0.567 h"
    private String horasEsperaDepositoEstimada;  // ej: "2.345 h"
    private String costoAproximado;         // ej: "$1500.50"

    private String distanciaKmReal;          // ej: "45.678 km"
    private String tiempoHorasReal;          // ej: "0.789 h"
    private String horasEsperaDepositoReal;  // ej: "2.123 h"
    private String costoReal;               // ej: "$1600.75"

    private Long camionId;
    private LocalDateTime fechaInicioReal;
    private LocalDateTime fechaFinReal;

    private String estado;
}
