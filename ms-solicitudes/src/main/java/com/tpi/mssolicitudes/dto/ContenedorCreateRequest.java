package com.tpi.mssolicitudes.dto;

import lombok.Data;

@Data
public class ContenedorCreateRequest {

    private String codigo;
    private String tipo;
    private Double capacidadKg;
    private Double pesoReal;
    private Double volumenReal;
    // este campo NO lo manda el front, lo completa ms-solicitudes antes
    private String estado;
}
