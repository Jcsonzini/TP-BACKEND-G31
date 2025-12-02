package com.tpi.mssolicitudes.dto;

import lombok.Data;

@Data
public class ContenedorCreateRequest {

    /**
     * Código del contenedor en ms-catalogo.
     * - El cliente puede no enviarlo.
     * - Si viene null o vacío, ms-solicitudes genera uno al crear la solicitud.
     */
    private String codigo;

    private String tipo;
    private Double capacidadKg;
    private Double pesoReal;
    private Double volumenReal;

    /**
     * Este campo NO lo manda el front.
     * Lo completa ms-solicitudes antes de llamar a ms-catalogo.
     */
    private String estado;
}
