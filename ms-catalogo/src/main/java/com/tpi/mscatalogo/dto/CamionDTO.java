package com.tpi.mscatalogo.dto;

public class CamionDTO {

    private Long id;
    private String patente;
    private String modelo;

    // Capacidad máxima de carga (kg)
    private Double capacidadKg;

    // Volumen útil en m3
    private Double volumenM3;

    // Litros de gasoil por km (promedio del camión)
    private Double consumoLitrosKm;

    // Costo base por km propio del camión
    private Double costoBaseKm;

    // Empresa transportista
    private String empresaTransportista;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Double getCapacidadKg() { return capacidadKg; }
    public void setCapacidadKg(Double capacidadKg) { this.capacidadKg = capacidadKg; }

    public Double getVolumenM3() { return volumenM3; }
    public void setVolumenM3(Double volumenM3) { this.volumenM3 = volumenM3; }

    public Double getConsumoLitrosKm() { return consumoLitrosKm; }
    public void setConsumoLitrosKm(Double consumoLitrosKm) { this.consumoLitrosKm = consumoLitrosKm; }

    public Double getCostoBaseKm() { return costoBaseKm; }
    public void setCostoBaseKm(Double costoBaseKm) { this.costoBaseKm = costoBaseKm; }

    public String getEmpresaTransportista() { return empresaTransportista; }
    public void setEmpresaTransportista(String empresaTransportista) { this.empresaTransportista = empresaTransportista; }
}
