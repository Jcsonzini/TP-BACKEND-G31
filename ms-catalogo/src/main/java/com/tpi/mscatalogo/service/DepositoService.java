package com.tpi.mscatalogo.service;

import com.tpi.mscatalogo.dto.DepositoDTO;
import java.util.List;

public interface DepositoService {

    DepositoDTO crear(DepositoDTO dto);

    DepositoDTO actualizar(Long id, DepositoDTO dto);

    void eliminar(Long id);

    DepositoDTO obtenerPorId(Long id);

    List<DepositoDTO> listar();
}
