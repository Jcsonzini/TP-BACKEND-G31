package com.tpi.mssolicitudes.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "solicitudes",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_solicitud_numero", columnNames = "numero_solicitud")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador humano: ej. SOL-0001 (único).
     */
    @Column(name = "numero_solicitud", nullable = false, length = 50)
    private String numeroSolicitud;

    /**
     * Código del contenedor tal como existe en ms-catalogo.
     * No hay FK a nivel DB, solo referencia lógica.
     */
    @Column(name = "contenedor_codigo", nullable = false, length = 100)
    private String contenedorCodigo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Origen
    @Column(name = "origen_direccion", nullable = false)
    private String origenDireccion;

    @Column(name = "origen_latitud")
    private Double origenLatitud;

    @Column(name = "origen_longitud")
    private Double origenLongitud;

    // Destino
    @Column(name = "destino_direccion", nullable = false)
    private String destinoDireccion;

    @Column(name = "destino_latitud")
    private Double destinoLatitud;

    @Column(name = "destino_longitud")
    private Double destinoLongitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoSolicitud estado;

    // Costos y tiempos estimados / reales
    private Double costoEstimado;
    private Double tiempoEstimadoHoras;

    private Double costoFinal;
    private Double tiempoRealHoras;

    /**
     * ID de la ruta en ms-logistica asociada a esta solicitud (cuando ya está calculada).
     */
    @Column(name = "ruta_asignada_id")
    private Long rutaAsignadaId;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;
}
