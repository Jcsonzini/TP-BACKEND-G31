package com.tpi.mssolicitudes.dto;

import lombok.Data;

@Data
public class ContenedorDTO {

    private Long id;
    private String codigo;
    private String tipo;          // TipoContenedor como String (DRY20, etc.)

    private Double capacidadKg;   // capacidad máxima de carga
    private Double pesoReal;      // peso del contenedor
    private Double volumenReal;   // volumen útil en m3

    private String estado;        // EstadoContenedor como String
    private Long depositoActualId;
}
