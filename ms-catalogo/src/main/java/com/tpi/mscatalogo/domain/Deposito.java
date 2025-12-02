package com.tpi.mscatalogo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "depositos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ej: "Depósito Córdoba Norte"
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, length = 80)
    private String localidad;

    @Column(nullable = false, length = 80)
    private String provincia;

    // Coordenadas aproximadas para Google Maps
    @Column(precision = 9, scale = 6)
    private BigDecimal latitud;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitud;

    // Capacidad de almacenaje en contenedores o m², según cómo lo definan
    private Integer capacidadMaxima;

    // Costo de estadía diario del contenedor en este depósito
    @Column(nullable = false)
    private Double costoEstadiaDiaria;
}
