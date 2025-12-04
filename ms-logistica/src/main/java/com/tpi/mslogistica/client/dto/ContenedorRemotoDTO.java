package com.tpi.mslogistica.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContenedorRemotoDTO {

    private String codigo;

    // Capacidad máxima en kg del contenedor
    private Double capacidadKg;

    // Volumen real (m3) que ocupa el contenedor
    private Double volumenReal;

    // Opcional: atributos útiles si más adelante hacés validación de dimensiones
    private Double largo;
    private Double ancho;
    private Double alto;

    private String tipo;   // por si necesitás TipoContenedor
}
