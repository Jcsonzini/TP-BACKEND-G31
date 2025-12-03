package com.tpi.mssolicitudes.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudDTO {

    private Long id;
    private String contenedorCodigo;

    private Long clienteId;
    private String clienteNombreCompleto;

    // Origen
    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    // Destino
    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;

    // EstadoSolicitud como String (BORRADOR, PROGRAMADA, EN_TRANSITO, ENTREGADA, CANCELADA...)
    private String estado;

    // Costos y tiempos
    private String costoEstimado;      // ej: "$104365.02"
    private String tiempoEstimadoHoras; // ej: "7.283 h"

    private String costoFinal;         // ej: "$104365.02"
    private String tiempoRealHoras;    // ej: "8.123 h"

    // Relación con ms-logistica
    private Long rutaAsignadaId;

    // Relación con ms-catalogo (tarifa/parámetros sistema)
    private Long tarifaId;

    // Auditoría
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaActualizacion;
}
