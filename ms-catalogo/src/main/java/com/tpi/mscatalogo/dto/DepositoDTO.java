package com.tpi.mscatalogo.dto;

public class DepositoDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String localidad;
    private String provincia;
    private Double latitud;
    private Double longitud;
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

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public Integer getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }

    public Double getCostoEstadiaDiaria() { return costoEstadiaDiaria; }
    public void setCostoEstadiaDiaria(Double costoEstadiaDiaria) { this.costoEstadiaDiaria = costoEstadiaDiaria; }
}
