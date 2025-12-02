package com.tpi.mssolicitudes.service;

import com.tpi.mssolicitudes.dto.ClienteDTO;

import java.util.List;

public interface ClienteService {

    ClienteDTO crear(ClienteDTO dto);

    ClienteDTO actualizar(Long id, ClienteDTO dto);

    void eliminar(Long id);

    ClienteDTO obtenerPorId(Long id);

    List<ClienteDTO> listar();
}
