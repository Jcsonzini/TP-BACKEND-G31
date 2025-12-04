package com.tpi.mslogistica.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudRemotaDTO {

    private Long id;

    // El código del contenedor asociado a esta solicitud
    private String contenedorCodigo;

    // Por si más adelante querés validar otras cosas:
    private String estado;
    private Long clienteId;
}
