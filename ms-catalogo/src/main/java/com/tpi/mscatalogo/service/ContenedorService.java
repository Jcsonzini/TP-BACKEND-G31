package com.tpi.mscatalogo.service;

import com.tpi.mscatalogo.dto.ContenedorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContenedorService {

    ContenedorDTO crear(ContenedorDTO dto);

    ContenedorDTO actualizar(String codigo, ContenedorDTO dto);

    void eliminar(String codigo);

    ContenedorDTO obtenerPorCodigo(String codigo);

    Page<ContenedorDTO> listar(Pageable pageable);
}
