package com.tpi.mssolicitudes.client.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RutaDTO {

    private Long id;
    private Long solicitudId;

    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;

    // Estimaciones (formateadas a 3 decimales)
    private String distanciaTotalKmEstimada;  // ej: "245.123 km"
    private String tiempoTotalHorasEstimada;  // ej: "3.456 h"
    private String costoTotalEstimado;        // ej: "$12345.67"

    // Valores reales (formateados)
    private String distanciaTotalKmReal;      // ej: "240.789 km"
    private String tiempoTotalHorasReal;      // ej: "3.234 h"
    private String costoTotalReal;            // ej: "$12456.89"

    private String estado;
    private LocalDateTime fechaCreacion;

    private List<TramoDTO> tramos;
}
