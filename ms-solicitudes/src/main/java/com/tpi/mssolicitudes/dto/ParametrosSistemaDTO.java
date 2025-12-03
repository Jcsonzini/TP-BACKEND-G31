package com.tpi.mssolicitudes.dto;

import lombok.Data;

@Data
public class ParametrosSistemaDTO {

    private Long id;
    private String nombre;
    private Double costoBaseKm;
    private Double costoEstadiaDiaria;
    private Double costoDescargaCarga;
    private Double costoTolerancia;
    private Double precioLitroCombustible;
    private Double consumoPromedioGeneral;
    private Boolean activa;
    private String descripcion;
}
