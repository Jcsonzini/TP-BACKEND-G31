package com.tpi.mslogistica.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RutaDTO {

    private Long id;

    // Referencia a la solicitud en ms-solicitudes
    private Long solicitudId;

    // Origen / Destino
    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;

    // Estimaciones (formateadas a 3 decimales)
    @JsonIgnore
    private String distanciaTotalKmEstimada;  // ej: "245.123 km" (no mostrar en API)
    private String tiempoTotalHorasEstimada;  // ej: "3.456 h"
    private String costoTotalEstimado;        // ej: "$12345.67"

    // Valores reales (formateados)
    @JsonIgnore
    private String distanciaTotalKmReal;      // ej: "240.789 km" (no mostrar en API)
    private String tiempoTotalHorasReal;      // ej: "3.234 h"
    private String costoTotalReal;            // ej: "$12456.89"

    // EstadoRuta como String (PLANIFICADA, EN_CURSO, COMPLETADA, CANCELADA)
    private String estado;

    private LocalDateTime fechaCreacion;

    // Tramos asociados, ordenados por "orden"
    private List<TramoDTO> tramos;
}
