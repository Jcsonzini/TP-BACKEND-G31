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

    // Nombre identificativo de la tarifa
    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    // ===== PARÁMETROS DE COSTO =====
    // Costo base por km del transporte ($/km)
    @Column(nullable = false)
    private Double costoBaseKm;

    // Costo de estadía diaria en depósitos ($/día)
    @Column(nullable = false)
    private Double costoEstadiaDiaria;

    // Costo de descarga/carga por operación ($/operación)
    @Column(nullable = false)
    private Double costoDescargaCarga;

    // Costo por tolerancia de demora ($/hora)
    @Column(nullable = false)
    private Double costoTolerancia;

    // ===== PARÁMETROS DE COMBUSTIBLE =====
    // Precio actual del litro de combustible
    @Column(nullable = false)
    private Double precioLitroCombustible;

    // Consumo promedio general (litros/km) que el enunciado dice que
    // se calcula en base al promedio de los camiones aptos
    @Column(nullable = false)
    private Double consumoPromedioGeneral;

    // ===== METADATA =====
    @Column(nullable = false)
    private Boolean activa = true;

    @Column(length = 255)
    private String descripcion;
}
