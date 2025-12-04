package com.tpi.mssolicitudes.dto;

import lombok.Data;

@Data
public class ParametrosSistemaDTO {

    private Long id;
    private String nombre;
    private Double costoDescargaCarga;
    private Double costoTolerancia;
    private Double precioLitroCombustible;
    private Boolean activa;
    private String descripcion;
}
