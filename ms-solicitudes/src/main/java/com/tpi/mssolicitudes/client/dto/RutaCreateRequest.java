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

    // ===== PARÁMETROS DE TARIFA =====
    // Costo base por km del transporte ($/km)
    private Double costoBaseKm;

    // Costo de estadía diaria en depósitos ($/día)
    private Double costoEstadiaDiaria;

    // Costo de descarga/carga por operación ($/operación)
    private Double costoDescargaCarga;

    // Costo por tolerancia de demora ($/hora)
    private Double costoTolerancia;

    // Precio actual del litro de combustible
    private Double precioLitroCombustible;

    // Consumo promedio general (litros/km)
    private Double consumoPromedioGeneral;
}
