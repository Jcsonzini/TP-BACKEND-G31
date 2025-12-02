package com.tpi.mslogistica.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rutas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia a la solicitud en ms-solicitudes
    @Column(nullable = false)
    private Long solicitudId;

    // Origen / Destino
    @Column(nullable = false)
    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    @Column(nullable = false)
    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;

    // Estimaciones
    private Double distanciaTotalKmEstimada;
    private Double tiempoTotalHorasEstimada;
    private Double costoTotalEstimado;

    // Valores reales (cuando se complete)
    private Double distanciaTotalKmReal;
    private Double tiempoTotalHorasReal;
    private Double costoTotalReal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoRuta estado;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    @Builder.Default
    private List<Tramo> tramos = new ArrayList<>();
}
