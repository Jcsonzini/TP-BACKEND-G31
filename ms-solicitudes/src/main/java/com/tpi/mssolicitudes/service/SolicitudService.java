package com.tpi.mssolicitudes.service;

import com.tpi.mssolicitudes.dto.CambioEstadoSolicitudRequest;
import com.tpi.mssolicitudes.dto.SolicitudCreateRequest;
import com.tpi.mssolicitudes.dto.SolicitudDTO;

import java.util.List;

public interface SolicitudService {

    SolicitudDTO crear(SolicitudCreateRequest request);

    SolicitudDTO actualizar(Long id, SolicitudDTO dto);

    void cancelar(Long id);

    SolicitudDTO cambiarEstado(Long id, CambioEstadoSolicitudRequest request);

    SolicitudDTO obtenerPorId(Long id);

    SolicitudDTO obtenerPorNumero(String numeroSolicitud);

    List<SolicitudDTO> listar();

    List<SolicitudDTO> listarPorCliente(Long clienteId);
}
