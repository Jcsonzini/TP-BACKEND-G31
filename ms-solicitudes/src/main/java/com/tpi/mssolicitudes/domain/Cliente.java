package com.tpi.mssolicitudes.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    private String telefono;

    /**
     * DNI / CUIT / etc. Podés decidir si lo hacés único o no.
     */
    @Column(name = "identificacion")
    private String identificacion;

    /**
     * Dirección de contacto del cliente (no origen/destino del envío).
     */
    private String direccion;
}
