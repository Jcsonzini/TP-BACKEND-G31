package com.tpi.mssolicitudes.client.dto;

import lombok.Data;

@Data
public class FinalizarOperacionRequest {
    private Long rutaId;
    private Double tiempoRealHoras;
    private Double costoTotalReal;
}
