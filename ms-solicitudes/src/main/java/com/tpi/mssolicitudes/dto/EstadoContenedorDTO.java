package com.tpi.mssolicitudes.dto;

import lombok.Data;

import com.tpi.mssolicitudes.domain.EstadoSolicitud; 
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EstadoContenedorDTO {

    private Long solicitudId;
    private String contenedorCodigo;

    private EstadoSolicitud estadoSolicitud;      // PROGRAMADA / PLANIFICADA / EN_TRANSITO / EN_DEPOSITO
    private EstadoContenedor estadoContenedor;    // DISPONIBLE / EN_DEPOSITO / EN_TRANSITO

    private Long rutaId;
    private String estadoRuta;                    // String para simplificar (PLANIFICADA / EN_CURSO / COMPLETADA)

    private String origen;
    private String destino;

    private String ubicacionActual;               // texto amigable

    private TramoResumenDTO tramoActual;          // si hay tramo en curso
    private List<TramoResumenDTO> tramos;        // resumen de todos los tramos

    private String clienteNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaActualizacion;
}
