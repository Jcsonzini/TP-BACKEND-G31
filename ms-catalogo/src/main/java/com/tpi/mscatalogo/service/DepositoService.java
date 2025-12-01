package com.tpi.mscatalogo.service;

import com.tpi.mscatalogo.dto.DepositoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepositoService {

    DepositoDTO crear(DepositoDTO dto);

    DepositoDTO actualizar(Long id, DepositoDTO dto);

    void eliminar(Long id);

    DepositoDTO obtenerPorId(Long id);

    Page<DepositoDTO> listar(Pageable pageable);
}
