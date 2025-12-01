package com.tpi.mscatalogo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parametros_sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParametrosSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Costo base por km del sistema (para usar de referencia)
    @Column(nullable = false)
    private Double costoBaseKm;

    // Precio actual del litro de combustible
    @Column(nullable = false)
    private Double precioLitroCombustible;

    // Consumo promedio general (litros/km) que el enunciado dice que
    // se calcula en base al promedio de los camiones aptos
    @Column(nullable = false)
    private Double consumoPromedioGeneral;
}
