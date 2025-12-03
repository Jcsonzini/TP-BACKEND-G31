package com.tpi.mslogistica.service;

import com.tpi.mslogistica.dto.AsignarCamionTramoRequest;
import com.tpi.mslogistica.dto.CambioEstadoTramoRequest;
import com.tpi.mslogistica.dto.TramoDTO;
import java.util.List;

public interface TramoService {

    TramoDTO obtenerPorId(Long id);

    List<TramoDTO> listarPorRuta(Long rutaId);

    TramoDTO asignarCamion(Long tramoId, AsignarCamionTramoRequest request);

    TramoDTO cambiarEstado(Long tramoId, CambioEstadoTramoRequest request);

    // A y B:
    TramoDTO iniciarTramo(Long tramoId);

    TramoDTO finalizarTramo(Long tramoId);
}
