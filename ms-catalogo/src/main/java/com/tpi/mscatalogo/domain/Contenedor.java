package com.tpi.mscatalogo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contenedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código único: puede ser el número del contenedor
    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoContenedor tipo;

    // Capacidad máxima de carga (kg)
    @Column(nullable = false)
    private Double capacidadKg;

    // Tara o peso real del contenedor vacío (kg)
    @Column(nullable = false)
    private Double pesoReal;

    // Volumen útil en m3
    @Column(nullable = false)
    private Double volumenReal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoContenedor estado;

    // Depósito actual donde se encuentra el contenedor (opcional)
    @ManyToOne
    @JoinColumn(name = "deposito_actual_id")
    private Deposito depositoActual;
}
