package com.tpi.mslogistica.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tramos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ruta a la que pertenece
    @ManyToOne(optional = false)
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;

    // Orden dentro de la ruta (1, 2, 3, ...)
    @Column(nullable = false)
    private Integer orden;

    // Puntos del tramo
    @Column(nullable = false)
    private String origenDescripcion;
    private Double origenLatitud;
    private Double origenLongitud;

    @Column(nullable = false)
    private String destinoDescripcion;
    private Double destinoLatitud;
    private Double destinoLongitud;

    // Estimaciones
    private Double distanciaKmEstimada;
    private Double tiempoHorasEstimada;
    private Double horasEsperaDepositoEstimada; // si aplica

    // Valores reales
    private Double distanciaKmReal;
    private Double tiempoHorasReal;
    private Double horasEsperaDepositoReal;

    // Camión asignado al tramo (viene de ms-catalogo)
    @Column(name = "camion_id")
    private Long camionId;

    // Tiempos reales del tramo
    private LocalDateTime fechaInicioReal;
    private LocalDateTime fechaFinReal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoTramo estado;
}
