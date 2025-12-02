package com.tpi.mscatalogo.service;

import com.tpi.mscatalogo.dto.ContenedorDTO;
import java.util.List;

public interface ContenedorService {

    ContenedorDTO crear(ContenedorDTO dto);

    ContenedorDTO actualizar(String codigo, ContenedorDTO dto);

    void eliminar(String codigo);

    ContenedorDTO obtenerPorCodigo(String codigo);

    List<ContenedorDTO> listar();
}
