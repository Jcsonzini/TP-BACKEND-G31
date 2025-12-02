package com.tpi.mslogistica.dto;

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

    // Estimaciones
    private Double distanciaTotalKmEstimada;
    private Double tiempoTotalHorasEstimada;
    private Double costoTotalEstimado;

    // Valores reales
    private Double distanciaTotalKmReal;
    private Double tiempoTotalHorasReal;
    private Double costoTotalReal;

    // EstadoRuta como String (PLANIFICADA, EN_CURSO, COMPLETADA, CANCELADA)
    private String estado;

    private LocalDateTime fechaCreacion;

    // Tramos asociados, ordenados por "orden"
    private List<TramoDTO> tramos;
}
