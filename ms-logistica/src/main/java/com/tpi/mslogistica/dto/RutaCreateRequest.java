package com.tpi.mslogistica.dto;

import lombok.Data;

@Data
public class RutaCreateRequest {

    // ID de la solicitud en ms-solicitudes
    private Long solicitudId;

    // Origen de la ruta (copiados de la Solicitud)
    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    // Destino de la ruta
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
