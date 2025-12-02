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

    private Double distanciaTotalKmEstimada;
    private Double tiempoTotalHorasEstimada;
    private Double costoTotalEstimado;

    private Double distanciaTotalKmReal;
    private Double tiempoTotalHorasReal;
    private Double costoTotalReal;

    private String estado;
    private LocalDateTime fechaCreacion;

    private List<TramoDTO> tramos;
}
