package com.tpi.mssolicitudes.client.dto;

import lombok.Data;

@Data
public class RutaCreateRequest {

    private Long solicitudId;

    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;

    // ===== PARÁMETROS DE TARIFA (solo los fijos) =====
    
    // Costo de descarga/carga por operación ($/operación)
    private Double costoDescargaCarga;

    // Costo por tolerancia de demora ($/hora)
    private Double costoTolerancia;

    // Precio actual del litro de combustible
    private Double precioLitroCombustible;

    // ===== PARÁMETROS CALCULADOS (promedios de camiones aptos) =====
    
    // Costo base por km promedio de los camiones aptos ($/km)
    private Double costoBaseKmPromedio;

    // Consumo promedio de los camiones aptos (litros/km)
    private Double consumoPromedioLitrosKm;

    // ===== DATOS DEL CONTENEDOR (para cálculo de estadía) =====
    
    // Peso del contenedor en kg (para buscar depósitos)
    private Double contenedorPesoKg;
    
    // Volumen del contenedor en m3
    private Double contenedorVolumenM3;
}
