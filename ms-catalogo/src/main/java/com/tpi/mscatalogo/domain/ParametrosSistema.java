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

    // ===== METADATA =====
    @Column(nullable = false)
    private Boolean activa = true;

    @Column(length = 255)
    private String descripcion;
    
    // NOTA: Los siguientes campos fueron ELIMINADOS porque ahora se calculan dinámicamente:
    // - costoBaseKm: se calcula como promedio de los camiones aptos para el contenedor
    // - costoEstadiaDiaria: se obtiene de cada depósito específico
    // - consumoPromedioGeneral: se calcula como promedio de los camiones aptos para el contenedor
}
