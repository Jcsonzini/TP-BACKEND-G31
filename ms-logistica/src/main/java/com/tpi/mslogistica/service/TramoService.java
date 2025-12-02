package com.tpi.mslogistica.service;

import com.tpi.mslogistica.dto.AsignarCamionTramoRequest;
import com.tpi.mslogistica.dto.CambioEstadoTramoRequest;
import com.tpi.mslogistica.dto.TramoDTO;

public interface TramoService {

    TramoDTO obtenerPorId(Long id);

    TramoDTO asignarCamion(Long tramoId, AsignarCamionTramoRequest request);

    TramoDTO cambiarEstado(Long tramoId, CambioEstadoTramoRequest request);
}
