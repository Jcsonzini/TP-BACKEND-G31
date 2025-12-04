package com.tpi.mslogistica.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamionRemotoDTO {

    private Long id;

    // Capacidad máxima de carga en kg
    private Double capacidadKg;

    // Volumen máximo disponible en m3
    private Double volumenM3;

    // Si querés agregar patente y modelo (opcionales):
    private String patente;
    private String modelo;

    // Si querés agregar otros campos del catálogo:
    private String empresaTransportista;
    private Double consumoLitrosKm;
    private Double costoBaseKm;
}
