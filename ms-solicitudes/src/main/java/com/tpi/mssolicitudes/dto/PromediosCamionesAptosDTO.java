package com.tpi.mssolicitudes.dto;

import lombok.Data;

/**
 * DTO que contiene los promedios calculados de los camiones aptos para transportar un contenedor.
 * Los promedios se calculan dinámicamente en base a los camiones que tienen capacidad (kg) y volumen (m³)
 * suficiente para el contenedor.
 */
@Data
public class PromediosCamionesAptosDTO {

    // Promedio de consumo de combustible (litros/km) de los camiones aptos
    private Double consumoPromedioLitrosKm;

    // Promedio de costo base por km de los camiones aptos
    private Double costoBaseKmPromedio;

    // Cantidad de camiones aptos encontrados
    private Integer cantidadCamionesAptos;
}
