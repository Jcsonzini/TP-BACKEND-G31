package com.tpi.mscatalogo.dto;

public class ParametrosSistemaDTO {

    private Long id;
    private Double costoBaseKm;
    private Double precioLitroCombustible;
    private Double consumoPromedioGeneral;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getCostoBaseKm() { return costoBaseKm; }
    public void setCostoBaseKm(Double costoBaseKm) { this.costoBaseKm = costoBaseKm; }

    public Double getPrecioLitroCombustible() { return precioLitroCombustible; }
    public void setPrecioLitroCombustible(Double precioLitroCombustible) { this.precioLitroCombustible = precioLitroCombustible; }
    public Double getConsumoPromedioGeneral() { return consumoPromedioGeneral; }
    public void setConsumoPromedioGeneral(Double consumoPromedioGeneral) { this.consumoPromedioGeneral = consumoPromedioGeneral; }
}
