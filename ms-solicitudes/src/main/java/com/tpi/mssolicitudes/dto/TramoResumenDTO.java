package com.tpi.mssolicitudes.dto;

import lombok.Data;

@Data
public class TramoResumenDTO {

    private Long tramoId;
    private Integer orden;
    private String origen;
    private String destino;
    private String estado; // lo tomamos como texto (PENDIENTE, ASIGNADO_A_CAMION, EN_CURSO, FINALIZADO)
}
