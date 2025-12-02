package com.tpi.mssolicitudes.dto;

import lombok.Data;

@Data
public class ClienteDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String identificacion;
    private String direccion;
}
