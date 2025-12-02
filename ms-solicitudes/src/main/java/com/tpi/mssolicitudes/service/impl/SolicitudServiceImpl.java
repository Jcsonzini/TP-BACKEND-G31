package com.tpi.mssolicitudes.service.impl;

import com.tpi.mssolicitudes.domain.Cliente;
import com.tpi.mssolicitudes.domain.EstadoSolicitud;
import com.tpi.mssolicitudes.domain.Solicitud;
import com.tpi.mssolicitudes.dto.CambioEstadoSolicitudRequest;
import com.tpi.mssolicitudes.dto.ClienteCreateRequest;
import com.tpi.mssolicitudes.dto.SolicitudCreateRequest;
import com.tpi.mssolicitudes.dto.SolicitudDTO;
import com.tpi.mssolicitudes.repository.ClienteRepository;
import com.tpi.mssolicitudes.repository.SolicitudRepository;
import com.tpi.mssolicitudes.service.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public SolicitudDTO crear(SolicitudCreateRequest request) {

        // 1. Resolver Cliente (id o creación)
        Cliente cliente = resolverCliente(request);

        // 2. Resolver Contenedor
        String contenedorCodigo = resolverContenedorCodigo(request);
        // más adelante acá se integrará ms-catalogo

        // 3. Crear entidad Solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setNumeroSolicitud(generarNumeroSolicitud());
        solicitud.setCliente(cliente);
        solicitud.setContenedorCodigo(contenedorCodigo);
        solicitud.setEstado(EstadoSolicitud.BORRADOR);

        solicitud.setOrigenDireccion(request.getOrigenDireccion());
        solicitud.setOrigenLatitud(request.getOrigenLatitud());
        solicitud.setOrigenLongitud(request.getOrigenLongitud());

        solicitud.setDestinoDireccion(request.getDestinoDireccion());
        solicitud.setDestinoLatitud(request.getDestinoLatitud());
        solicitud.setDestinoLongitud(request.getDestinoLongitud());

        Solicitud guardada = solicitudRepository.save(solicitud);
        return toDTO(guardada);
    }

    @Override
    public SolicitudDTO actualizar(Long id, SolicitudDTO dto) {
        Solicitud existente = solicitudRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada con id " + id));

        if (dto.getOrigenDireccion() != null) {
            existente.setOrigenDireccion(dto.getOrigenDireccion());
            existente.setOrigenLatitud(dto.getOrigenLatitud());
            existente.setOrigenLongitud(dto.getOrigenLongitud());
        }

        if (dto.getDestinoDireccion() != null) {
            existente.setDestinoDireccion(dto.getDestinoDireccion());
            existente.setDestinoLatitud(dto.getDestinoLatitud());
            existente.setDestinoLongitud(dto.getDestinoLongitud());
        }

        if (dto.getEstado() != null) {
            existente.setEstado(EstadoSolicitud.valueOf(dto.getEstado()));
        }

        existente.setCostoEstimado(dto.getCostoEstimado());
        existente.setTiempoEstimadoHoras(dto.getTiempoEstimadoHoras());
        existente.setCostoFinal(dto.getCostoFinal());
        existente.setTiempoRealHoras(dto.getTiempoRealHoras());
        existente.setRutaAsignadaId(dto.getRutaAsignadaId());

        Solicitud actualizada = solicitudRepository.save(existente);
        return toDTO(actualizada);
    }

    @Override
    public void cancelar(Long id) {
        Solicitud existente = solicitudRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada con id " + id));

        existente.setEstado(EstadoSolicitud.CANCELADA);
        solicitudRepository.save(existente);
    }

    @Override
    public SolicitudDTO cambiarEstado(Long id, CambioEstadoSolicitudRequest request) {
        Solicitud existente = solicitudRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada con id " + id));

        EstadoSolicitud nuevo = EstadoSolicitud.valueOf(request.getNuevoEstado());
        existente.setEstado(nuevo);

        Solicitud guardada = solicitudRepository.save(existente);
        return toDTO(guardada);
    }

    @Override
    public SolicitudDTO obtenerPorId(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada con id " + id));
        return toDTO(solicitud);
    }

    @Override
    public SolicitudDTO obtenerPorNumero(String numeroSolicitud) {
        Solicitud solicitud = solicitudRepository.findByNumeroSolicitud(numeroSolicitud)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada con número " + numeroSolicitud));
        return toDTO(solicitud);
    }

    @Override
    public List<SolicitudDTO> listar() {
        Page<Solicitud> page = solicitudRepository.findAll(Pageable.unpaged());
        return page.getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudDTO> listarPorCliente(Long clienteId) {
        Page<Solicitud> page = solicitudRepository.findByCliente_Id(clienteId, Pageable.unpaged());
        return page.getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // =============== Helpers ===================

    private Cliente resolverCliente(SolicitudCreateRequest request) {

        if (request.getClienteId() != null) {
            return clienteRepository.findById(request.getClienteId())
                    .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado con id " + request.getClienteId()));
        }

        ClienteCreateRequest c = request.getCliente();
        if (c == null) {
            throw new IllegalArgumentException("Debe indicar clienteId o datos del cliente");
        }

        Cliente nuevo = new Cliente();
        nuevo.setNombre(c.getNombre());
        nuevo.setApellido(c.getApellido());
        nuevo.setEmail(c.getEmail());
        nuevo.setTelefono(c.getTelefono());
        nuevo.setIdentificacion(c.getIdentificacion());
        nuevo.setDireccion(c.getDireccion());

        return clienteRepository.save(nuevo);
    }

    private String resolverContenedorCodigo(SolicitudCreateRequest request) {
        if (request.getContenedorCodigo() != null) {
            // más adelante validaremos contra ms-catalogo
            return request.getContenedorCodigo();
        }

        if (request.getContenedor() != null) {
            // MÁS ADELANTE: llamada a ms-catalogo para crear el contenedor
            // por ahora usamos el código que venga en el request
            return request.getContenedor().getCodigo();
        }

        throw new IllegalArgumentException("Debe indicar contenedorCodigo o datos del contenedor");
    }

    private String generarNumeroSolicitud() {
        return "SOL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private SolicitudDTO toDTO(Solicitud entity) {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setId(entity.getId());
        dto.setNumeroSolicitud(entity.getNumeroSolicitud());
        dto.setContenedorCodigo(entity.getContenedorCodigo());

        if (entity.getCliente() != null) {
            dto.setClienteId(entity.getCliente().getId());
            dto.setClienteNombreCompleto(
                    entity.getCliente().getNombre() + " " + entity.getCliente().getApellido()
            );
        }

        dto.setOrigenDireccion(entity.getOrigenDireccion());
        dto.setOrigenLatitud(entity.getOrigenLatitud());
        dto.setOrigenLongitud(entity.getOrigenLongitud());

        dto.setDestinoDireccion(entity.getDestinoDireccion());
        dto.setDestinoLatitud(entity.getDestinoLatitud());
        dto.setDestinoLongitud(entity.getDestinoLongitud());

        dto.setEstado(entity.getEstado() != null ? entity.getEstado().name() : null);

        dto.setCostoEstimado(entity.getCostoEstimado());
        dto.setTiempoEstimadoHoras(entity.getTiempoEstimadoHoras());
        dto.setCostoFinal(entity.getCostoFinal());
        dto.setTiempoRealHoras(entity.getTiempoRealHoras());

        dto.setRutaAsignadaId(entity.getRutaAsignadaId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaUltimaActualizacion(entity.getFechaUltimaActualizacion());

        return dto;
    }
}
