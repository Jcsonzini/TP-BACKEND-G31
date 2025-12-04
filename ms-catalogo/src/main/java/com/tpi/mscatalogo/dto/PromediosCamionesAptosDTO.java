package com.tpi.mscatalogo.dto;

/**
 * DTO que contiene los promedios calculados de los camiones aptos para transportar un contenedor.
 * Los promedios se calculan dinámicamente en base a los camiones que tienen capacidad (kg) y volumen (m³)
 * suficiente para el contenedor.
 */
public class PromediosCamionesAptosDTO {

    // Promedio de consumo de combustible (litros/km) de los camiones aptos
    private Double consumoPromedioLitrosKm;

    // Promedio de costo base por km de los camiones aptos
    private Double costoBaseKmPromedio;

    // Cantidad de camiones aptos encontrados
    private Integer cantidadCamionesAptos;

    public PromediosCamionesAptosDTO() {
    }

    public PromediosCamionesAptosDTO(Double consumoPromedioLitrosKm, Double costoBaseKmPromedio, Integer cantidadCamionesAptos) {
        this.consumoPromedioLitrosKm = consumoPromedioLitrosKm;
        this.costoBaseKmPromedio = costoBaseKmPromedio;
        this.cantidadCamionesAptos = cantidadCamionesAptos;
    }

    public Double getConsumoPromedioLitrosKm() {
        return consumoPromedioLitrosKm;
    }

    public void setConsumoPromedioLitrosKm(Double consumoPromedioLitrosKm) {
        this.consumoPromedioLitrosKm = consumoPromedioLitrosKm;
    }

    public Double getCostoBaseKmPromedio() {
        return costoBaseKmPromedio;
    }

    public void setCostoBaseKmPromedio(Double costoBaseKmPromedio) {
        this.costoBaseKmPromedio = costoBaseKmPromedio;
    }

    public Integer getCantidadCamionesAptos() {
        return cantidadCamionesAptos;
    }

    public void setCantidadCamionesAptos(Integer cantidadCamionesAptos) {
        this.cantidadCamionesAptos = cantidadCamionesAptos;
    }
}
