package com.tpi.mssolicitudes.service;

import com.tpi.mssolicitudes.client.dto.RutaDTO;
import com.tpi.mssolicitudes.domain.EstadoSolicitud;
import com.tpi.mssolicitudes.dto.CambioEstadoSolicitudRequest;
import com.tpi.mssolicitudes.dto.EstadoContenedorDTO;
import com.tpi.mssolicitudes.dto.EstadoContenedor;
import com.tpi.mssolicitudes.dto.SolicitudCreateRequest;
import com.tpi.mssolicitudes.dto.SolicitudDTO;
import com.tpi.mssolicitudes.client.dto.FinalizarOperacionRequest;

import java.util.List;

public interface SolicitudService {

    SolicitudDTO crear(SolicitudCreateRequest request);

    SolicitudDTO actualizar(Long id, SolicitudDTO dto);

    void cancelar(Long id);

    SolicitudDTO cambiarEstado(Long id, CambioEstadoSolicitudRequest request);

    SolicitudDTO obtenerPorId(Long id);

    List<SolicitudDTO> listar();

    List<SolicitudDTO> listarPorCliente(Long clienteId);

    /**
     * Genera rutas tentativas en ms-logistica para una solicitud existente.
     *
     * @param solicitudId id de la solicitud
     */
    List<RutaDTO> generarRutasParaSolicitud(Long solicitudId);

    /**
     * Asigna una ruta definitiva para la solicitud y la marca como planificada.
     */
    SolicitudDTO asignarRuta(Long solicitudId, Long rutaId);

    SolicitudDTO finalizarOperacion(Long solicitudId, FinalizarOperacionRequest request);

    SolicitudDTO marcarEnTransito(Long solicitudId);

    SolicitudDTO marcarEnDeposito(Long solicitudId);

    /**
     * Versión "simple" (ya existente): permite filtrar por destino y estado de la solicitud.
     */
    List<EstadoContenedorDTO> obtenerContenedoresPendientes(String destinoFiltro);

    /**
     * Versión "completa": permite filtrar además por estado lógico del contenedor y por cliente.
     */
    List<EstadoContenedorDTO> obtenerContenedoresPendientes(
            EstadoContenedor estadoContFiltro,
            String destinoFiltro,
            Long clienteIdFiltro,
            String ubicacionFiltro
    );

    EstadoContenedorDTO consultarEstadoTransporteContenedor(String contenedorCodigo);

}
