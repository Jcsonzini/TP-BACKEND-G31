package com.tpi.mscatalogo.service;

import com.tpi.mscatalogo.dto.CamionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CamionService {

    CamionDTO crear(CamionDTO dto);

    CamionDTO actualizar(String patente, CamionDTO dto);

    void eliminar(String patente);

    CamionDTO obtenerPorPatente(String patente);

    Page<CamionDTO> listar(Pageable pageable);
}
