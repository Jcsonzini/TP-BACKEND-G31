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

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;

    // NOTA: de momento NO integramos con ms-logistica en el alta.
    // Cuando quieras, se puede agregar un método específico para eso.

    @Override
    public SolicitudDTO crear(SolicitudCreateRequest request) {

        Objects.requireNonNull(request, "El request no puede ser nulo");

        // 1. Resolver Cliente (id o creación)
        Cliente cliente = resolverCliente(request);

        // 2. Resolver Contenedor (solo código, sin ubicación ni depósito)
        String contenedorCodigo = resolverContenedorCodigo(request);

        // 3. Crear entidad Solicitud en estado BORRADOR
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

        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setFechaUltimaActualizacion(LocalDateTime.now());

        // 4. Guardar solicitud (sin crear ruta todavía)
        Solicitud guardada = solicitudRepository.save(solicitud);

        // En esta etapa NO llamamos a ms-logistica.
        // Más adelante podés tener un endpoint del tipo:
        // POST /api/solicitudes/{id}/generar-ruta
        // que llame a logistica, cree la ruta y actualice:
        // - rutaAsignadaId
        // - costoEstimado
        // - tiempoEstimadoHoras

        return toDTO(guardada);
    }

    @Override
    public SolicitudDTO actualizar(Long id, SolicitudDTO dto) {
        Long nonNullId = Objects.requireNonNull(id, "El id de la solicitud no puede ser nulo");
        Solicitud existente = solicitudRepository.findById(nonNullId)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada con id " + nonNullId));

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

        existente.setFechaUltimaActualizacion(LocalDateTime.now());

        Solicitud actualizada = solicitudRepository.save(existente);
        return toDTO(actualizada);
    }

    @Override
    public void cancelar(Long id) {
        Long nonNullId = Objects.requireNonNull(id, "El id de la solicitud no puede ser nulo");
        Solicitud existente = solicitudRepository.findById(nonNullId)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada con id " + nonNullId));

        existente.setEstado(EstadoSolicitud.CANCELADA);
        existente.setFechaUltimaActualizacion(LocalDateTime.now());

        solicitudRepository.save(existente);
    }

    @Override
    public SolicitudDTO cambiarEstado(Long id, CambioEstadoSolicitudRequest request) {
        Long nonNullId = Objects.requireNonNull(id, "El id de la solicitud no puede ser nulo");
        Solicitud existente = solicitudRepository.findById(nonNullId)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada con id " + nonNullId));

        EstadoSolicitud nuevo = EstadoSolicitud.valueOf(request.getNuevoEstado());
        existente.setEstado(nuevo);
        existente.setFechaUltimaActualizacion(LocalDateTime.now());

        Solicitud guardada = solicitudRepository.save(existente);
        return toDTO(guardada);
    }

    @Override
    public SolicitudDTO obtenerPorId(Long id) {
        Long nonNullId = Objects.requireNonNull(id, "El id de la solicitud no puede ser nulo");
        Solicitud solicitud = solicitudRepository.findById(nonNullId)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada con id " + nonNullId));
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
        Long nonNullClienteId = Objects.requireNonNull(clienteId, "El id del cliente no puede ser nulo");
        Page<Solicitud> page = solicitudRepository.findByCliente_Id(nonNullClienteId, Pageable.unpaged());
        return page.getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // =============== Helpers ===================

    private Cliente resolverCliente(SolicitudCreateRequest request) {

        if (request.getClienteId() != null) {
            Long clienteId = Objects.requireNonNull(request.getClienteId());
            return clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado con id " + clienteId));
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
            return request.getContenedorCodigo();
        }

        if (request.getContenedor() != null) {
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
