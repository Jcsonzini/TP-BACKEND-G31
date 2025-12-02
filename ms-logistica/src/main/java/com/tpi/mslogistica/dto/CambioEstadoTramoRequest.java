package com.tpi.mslogistica.dto;

import lombok.Data;

@Data
public class CambioEstadoTramoRequest {

    /**
     * Valores permitidos por ahora: INICIAR, FINALIZAR
     */
    private String accion;
}
