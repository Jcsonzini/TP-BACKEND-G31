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
    private Double costoEstimado;
    private Double tiempoEstimadoHoras;

    private Double costoFinal;
    private Double tiempoRealHoras;
    private Double tarifa;

    // Relación con ms-logistica
    private Long rutaAsignadaId;

    // Auditoría
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaActualizacion;
}
