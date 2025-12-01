package com.tpi.mscatalogo.dto;

public class ContenedorDTO {

    private Long id;
    private String codigo;
    private String tipo;          // TipoContenedor como String (DRY20, etc.)

    private Double capacidadKg;   // capacidad máxima de carga
    private Double pesoReal;      // peso del contenedor
    private Double volumenReal;   // volumen útil en m3

    private String estado;        // EstadoContenedor como String
    private Long depositoActualId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Double getCapacidadKg() { return capacidadKg; }
    public void setCapacidadKg(Double capacidadKg) { this.capacidadKg = capacidadKg; }

    public Double getPesoReal() { return pesoReal; }
    public void setPesoReal(Double pesoReal) { this.pesoReal = pesoReal; }

    public Double getVolumenReal() { return volumenReal; }
    public void setVolumenReal(Double volumenReal) { this.volumenReal = volumenReal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Long getDepositoActualId() { return depositoActualId; }
    public void setDepositoActualId(Long depositoActualId) { this.depositoActualId = depositoActualId; }
}
