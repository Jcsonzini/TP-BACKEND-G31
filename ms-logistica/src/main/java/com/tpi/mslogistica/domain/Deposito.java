package com.tpi.mslogistica.domain;

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

    private String nombre;
    private String direccion;
    private String localidad;
    private String provincia;

    private BigDecimal latitud;
    private BigDecimal longitud;

    private Integer capacidadMaxima;
    private Double costoEstadiaDiaria;
}
