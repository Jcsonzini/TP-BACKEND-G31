package com.tpi.mscatalogo.service;

import com.tpi.mscatalogo.dto.CamionDTO;
import java.util.List;

public interface CamionService {

    CamionDTO crear(CamionDTO dto);

    CamionDTO actualizar(String patente, CamionDTO dto);

    void eliminar(String patente);

    CamionDTO obtenerPorPatente(String patente);

    List<CamionDTO> listar();
}
