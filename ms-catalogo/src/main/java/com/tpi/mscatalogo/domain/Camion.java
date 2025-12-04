package com.tpi.mscatalogo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "camiones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String patente;

    @Column(nullable = false, length = 80)
    private String modelo;

    // Capacidad máxima de carga (kg)
    @Column(nullable = false)
    private Double capacidadKg;

    // Volumen util en m3
    @Column(nullable = false)
    private Double volumenM3;

    // Litros de gasoil por km (promedio del camión)
    @Column(nullable = false)
    private Double consumoLitrosKm;

    // Costo base por km propio del camión (puede servir para matchear con el base del sistema)
    @Column(nullable = false)
    private Double costoBaseKm;

    // Empresa transportista a la que pertenece
    @Column(length = 120)
    private String empresaTransportista;

    // ===== DATOS DEL TRANSPORTISTA (CHOFER) =====
    
    @Column(length = 80)
    private String transportistaNombre;

    @Column(length = 80)
    private String transportistaApellido;

    @Column(length = 15)
    private String transportistaDni;

    @Column(length = 30)
    private String transportistaLicencia;  // Número de licencia de conducir

    @Column(length = 100)
    private String transportistaTelefono;

    @Column(length = 150)
    private String transportistaEmail;
}
