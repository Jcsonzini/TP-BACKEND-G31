package com.tpi.mssolicitudes.dto;

import lombok.Data;

@Data
public class CambioEstadoSolicitudRequest {

    /**
     * Valor del enum EstadoSolicitud: BORRADOR, PROGRAMADA, EN_TRANSITO, ENTREGADA, CANCELADA...
     */
    private String nuevoEstado;
}
