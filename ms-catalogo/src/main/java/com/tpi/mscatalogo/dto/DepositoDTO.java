package com.tpi.mscatalogo.dto;

import java.math.BigDecimal;

public class DepositoDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String localidad;
    private String provincia;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private Integer capacidadMaxima;
    private Double costoEstadiaDiaria;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public BigDecimal getLatitud() { return latitud; }
    public void setLatitud(BigDecimal latitud) { this.latitud = latitud; }

    public BigDecimal getLongitud() { return longitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }

    public Integer getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }

    public Double getCostoEstadiaDiaria() { return costoEstadiaDiaria; }
    public void setCostoEstadiaDiaria(Double costoEstadiaDiaria) { this.costoEstadiaDiaria = costoEstadiaDiaria; }
}
