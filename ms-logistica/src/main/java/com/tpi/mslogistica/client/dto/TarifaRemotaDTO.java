package com.tpi.mslogistica.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaRemotaDTO {

    private Long id;
    private String nombre;
    
    // Costo base por km del transporte ($/km)
    private Double costoBaseKm;
    
    // Costo de estadía diaria en depósitos ($/día) - valor por defecto si el depósito no tiene uno propio
    private Double costoEstadiaDiaria;
    
    // Costo de descarga/carga por operación ($/operación)
    private Double costoDescargaCarga;
    
    // Costo por tolerancia de demora ($/hora)
    private Double costoTolerancia;
    
    // Precio actual del litro de combustible
    private Double precioLitroCombustible;
    
    // Consumo promedio general (litros/km)
    private Double consumoPromedioGeneral;
    
    private Boolean activa;
}
