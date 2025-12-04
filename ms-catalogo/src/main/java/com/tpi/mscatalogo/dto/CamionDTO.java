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

    // Datos del transportista (chofer)
    private String transportistaNombre;
    private String transportistaApellido;
    private String transportistaDni;
    private String transportistaLicencia;
    private String transportistaTelefono;
    private String transportistaEmail;

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

    public String getTransportistaNombre() { return transportistaNombre; }
    public void setTransportistaNombre(String transportistaNombre) { this.transportistaNombre = transportistaNombre; }

    public String getTransportistaApellido() { return transportistaApellido; }
    public void setTransportistaApellido(String transportistaApellido) { this.transportistaApellido = transportistaApellido; }

    public String getTransportistaDni() { return transportistaDni; }
    public void setTransportistaDni(String transportistaDni) { this.transportistaDni = transportistaDni; }

    public String getTransportistaLicencia() { return transportistaLicencia; }
    public void setTransportistaLicencia(String transportistaLicencia) { this.transportistaLicencia = transportistaLicencia; }

    public String getTransportistaTelefono() { return transportistaTelefono; }
    public void setTransportistaTelefono(String transportistaTelefono) { this.transportistaTelefono = transportistaTelefono; }

    public String getTransportistaEmail() { return transportistaEmail; }
    public void setTransportistaEmail(String transportistaEmail) { this.transportistaEmail = transportistaEmail; }
}
