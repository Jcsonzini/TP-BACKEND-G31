package com.tpi.mscatalogo.dto;

public class ParametrosSistemaDTO {

    private Long id;
    private String nombre;
    private Double costoBaseKm;
    private Double costoEstadiaDiaria;
    private Double costoDescargaCarga;
    private Double costoTolerancia;
    private Double precioLitroCombustible;
    private Double consumoPromedioGeneral;
    private Boolean activa;
    private String descripcion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getCostoBaseKm() { return costoBaseKm; }
    public void setCostoBaseKm(Double costoBaseKm) { this.costoBaseKm = costoBaseKm; }

    public Double getCostoEstadiaDiaria() { return costoEstadiaDiaria; }
    public void setCostoEstadiaDiaria(Double costoEstadiaDiaria) { this.costoEstadiaDiaria = costoEstadiaDiaria; }

    public Double getCostoDescargaCarga() { return costoDescargaCarga; }
    public void setCostoDescargaCarga(Double costoDescargaCarga) { this.costoDescargaCarga = costoDescargaCarga; }

    public Double getCostoTolerancia() { return costoTolerancia; }
    public void setCostoTolerancia(Double costoTolerancia) { this.costoTolerancia = costoTolerancia; }

    public Double getPrecioLitroCombustible() { return precioLitroCombustible; }
    public void setPrecioLitroCombustible(Double precioLitroCombustible) { this.precioLitroCombustible = precioLitroCombustible; }

    public Double getConsumoPromedioGeneral() { return consumoPromedioGeneral; }
    public void setConsumoPromedioGeneral(Double consumoPromedioGeneral) { this.consumoPromedioGeneral = consumoPromedioGeneral; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
