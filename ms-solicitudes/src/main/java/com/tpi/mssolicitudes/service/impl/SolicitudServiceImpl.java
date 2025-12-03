package com.tpi.mssolicitudes.service.impl;

import com.tpi.mssolicitudes.domain.Cliente;
import com.tpi.mssolicitudes.domain.EstadoSolicitud;
import com.tpi.mssolicitudes.domain.Solicitud;
import com.tpi.mssolicitudes.dto.CambioEstadoSolicitudRequest;
import com.tpi.mssolicitudes.dto.ClienteCreateRequest;
import com.tpi.mssolicitudes.dto.ContenedorCreateRequest;
import com.tpi.mssolicitudes.dto.SolicitudCreateRequest;
import com.tpi.mssolicitudes.dto.SolicitudDTO;
import com.tpi.mssolicitudes.repository.ClienteRepository;
import com.tpi.mssolicitudes.repository.SolicitudRepository;
import com.tpi.mssolicitudes.service.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.tpi.mssolicitudes.client.CatalogoClient;
import com.tpi.mssolicitudes.client.dto.FinalizarOperacionRequest;
import com.tpi.mssolicitudes.client.dto.RutaCreateRequest;
import com.tpi.mssolicitudes.client.dto.RutaDTO;
import com.tpi.mssolicitudes.client.LogisticaClient;

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
    private final CatalogoClient catalogoClient;
    private final LogisticaClient logisticaClient;

    // NOTA: de momento NO integramos con ms-logistica en el alta.
    // Más adelante se agregará un endpoint específico para generar rutas.

    @Override
    public SolicitudDTO crear(SolicitudCreateRequest request) {

        Objects.requireNonNull(request, "El request no puede ser nulo");

        // 1. Resolver Cliente (id o creación)
        Cliente cliente = resolverCliente(request);

        // 2. Crear contenedor en el contexto de la solicitud
        //    Genera un código y (en el futuro) llamará a ms-catalogo
        String contenedorCodigo = crearContenedorDesdeSolicitud(request.getContenedor());

        // 3. Crear entidad Solicitud en estado BORRADOR
        Solicitud solicitud = new Solicitud();
        solicitud.setCliente(cliente);
        solicitud.setContenedorCodigo(contenedorCodigo);
        solicitud.setEstado(EstadoSolicitud.BORRADOR);

        // Origen
        solicitud.setOrigenDireccion(request.getOrigenDireccion());
        solicitud.setOrigenLatitud(request.getOrigenLatitud());
        solicitud.setOrigenLongitud(request.getOrigenLongitud());

        // Destino
        solicitud.setDestinoDireccion(request.getDestinoDireccion());
        solicitud.setDestinoLatitud(request.getDestinoLatitud());
        solicitud.setDestinoLongitud(request.getDestinoLongitud());

        Double tiempoEstimado = estimarTiempoHoras(
            solicitud.getOrigenLatitud(),
            solicitud.getOrigenLongitud(),
            solicitud.getDestinoLatitud(),
            solicitud.getDestinoLongitud()
        );
        solicitud.setTiempoEstimadoHoras(tiempoEstimado);
        solicitud.setTiempoRealHoras(null);
        solicitud.setTarifa(null);

        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setFechaUltimaActualizacion(LocalDateTime.now());

        // 4. Guardar solicitud (sin crear ruta todavía)
        Solicitud guardada = solicitudRepository.save(solicitud);

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
        existente.setTarifa(dto.getTarifa());


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

    @Override
    public List<RutaDTO> generarRutasParaSolicitud(Long solicitudId) {

        Long nonNullId = Objects.requireNonNull(solicitudId,
                "El id de la solicitud no puede ser nulo");

        Solicitud solicitud = solicitudRepository.findById(nonNullId)
                .orElseThrow(() ->
                        new NoSuchElementException("Solicitud no encontrada con id " + nonNullId));

        // Validar que tenga origen y destino completos
        if (solicitud.getOrigenDireccion() == null
                || solicitud.getOrigenLatitud() == null
                || solicitud.getOrigenLongitud() == null
                || solicitud.getDestinoDireccion() == null
                || solicitud.getDestinoLatitud() == null
                || solicitud.getDestinoLongitud() == null) {
            throw new IllegalStateException(
                    "La solicitud no tiene origen/destino completos para generar rutas");
        }

        // Armar el request para ms-logistica
        RutaCreateRequest request = new RutaCreateRequest();
        request.setSolicitudId(solicitud.getId());
        request.setOrigenDireccion(solicitud.getOrigenDireccion());
        request.setOrigenLatitud(solicitud.getOrigenLatitud());
        request.setOrigenLongitud(solicitud.getOrigenLongitud());
        request.setDestinoDireccion(solicitud.getDestinoDireccion());
        request.setDestinoLatitud(solicitud.getDestinoLatitud());
        request.setDestinoLongitud(solicitud.getDestinoLongitud());

        // llamada a ms-logistica usando el cliente HTTP (WebClient por adentro)
        return logisticaClient.generarRutasTentativas(request);
    }

    @Override
    public SolicitudDTO asignarRuta(Long solicitudId, Long rutaId) {

        Long nonNullSolicitudId = Objects.requireNonNull(solicitudId, "El id de la solicitud no puede ser nulo");
        Long nonNullRutaId = Objects.requireNonNull(rutaId, "El id de la ruta no puede ser nulo");

        Solicitud solicitud = solicitudRepository.findById(nonNullSolicitudId)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada: " + nonNullSolicitudId));

        RutaDTO rutaSeleccionada = logisticaClient.seleccionarRuta(nonNullRutaId);

        if (rutaSeleccionada != null && rutaSeleccionada.getSolicitudId() != null
                && !rutaSeleccionada.getSolicitudId().equals(solicitud.getId())) {
            throw new IllegalStateException("La ruta " + nonNullRutaId + " no pertenece a la solicitud " + nonNullSolicitudId);
        }

        solicitud.setRutaAsignadaId(nonNullRutaId);
        solicitud.setEstado(EstadoSolicitud.PLANIFICADA);
        solicitud.setFechaUltimaActualizacion(LocalDateTime.now());

        Solicitud guardada = solicitudRepository.save(solicitud);
        return toDTO(guardada);
    }

    @Override
    public SolicitudDTO finalizarOperacion(Long solicitudId, FinalizarOperacionRequest request) {

        Long nonNullSolicitudId = Objects.requireNonNull(solicitudId, "El id de la solicitud no puede ser nulo");
        FinalizarOperacionRequest nonNullRequest = Objects.requireNonNull(request, "El request no puede ser nulo");

        Solicitud solicitud = solicitudRepository.findById(nonNullSolicitudId)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada: " + nonNullSolicitudId));

        // Solo se puede finalizar si está PLANIFICADA o EN_TRANSITO
        if (solicitud.getEstado() != EstadoSolicitud.PLANIFICADA &&
            solicitud.getEstado() != EstadoSolicitud.EN_TRANSITO) {

            throw new IllegalStateException(
                    "No se puede finalizar una solicitud en estado: " + solicitud.getEstado());
        }

        // Cambiamos estado → ENTREGADA
        solicitud.setEstado(EstadoSolicitud.ENTREGADA);

        // Datos reales enviados por ms-logistica
        if (nonNullRequest.getRutaId() != null) {
            solicitud.setRutaAsignadaId(nonNullRequest.getRutaId());
        }
        solicitud.setTiempoRealHoras(nonNullRequest.getTiempoRealHoras());
        solicitud.setCostoFinal(nonNullRequest.getCostoTotalReal());
        solicitud.setFechaUltimaActualizacion(LocalDateTime.now());

        solicitudRepository.save(solicitud);

        return toDTO(solicitud);
    }


 
    // ===================== Helpers ===========================

    private Cliente resolverCliente(SolicitudCreateRequest request) {

        if (request.getClienteId() != null) {
            Long clienteId = Objects.requireNonNull(request.getClienteId(), "El id del cliente no puede ser nulo");
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

    /**
     * El contenedor se crea lógicamente con la solicitud.
     * - Si viene null -> error.
     * - Si no trae código, generamos uno (CONT-XXXXXX).
     * - Guardamos ese código en la solicitud.
     * - Más adelante, acá se llamará a ms-catalogo para persistir el contenedor real.
     */
    private String crearContenedorDesdeSolicitud(ContenedorCreateRequest cont) {
        if (cont == null) {
            throw new IllegalArgumentException(
                    "Debe indicar los datos del contenedor, que se crea junto con la solicitud"
            );
        }

        // 1) Si no viene código, lo generamos acá
        String codigo = cont.getCodigo();
        if (codigo == null || codigo.isBlank()) {
            codigo = "CONT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            cont.setCodigo(codigo);
        }

        // 2) Estado inicial lógico del contenedor
        cont.setEstado("PENDIENTE");

        // 3) Crear el contenedor REAL en ms-catalogo usando tu CatalogoClient
        // OJO: ajustá el nombre del método a lo que ya tengas en tu interface
        catalogoClient.crearContenedor(cont);

        // 4) En ms-solicitudes solo guardamos y devolvemos el código
        return codigo;
    }

    private SolicitudDTO toDTO(Solicitud entity) {

        SolicitudDTO dto = new SolicitudDTO();
        dto.setId(entity.getId());
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
        dto.setTarifa(entity.getTarifa());

        dto.setRutaAsignadaId(entity.getRutaAsignadaId());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaUltimaActualizacion(entity.getFechaUltimaActualizacion());


        return dto;
    }

    private Double estimarTiempoHoras(Double origenLatitud,
                                      Double origenLongitud,
                                      Double destinoLatitud,
                                      Double destinoLongitud) {

        if (origenLatitud == null || origenLongitud == null
                || destinoLatitud == null || destinoLongitud == null) {
            return null;
        }

        double radioTierraKm = 6371.0;
        double lat1Rad = Math.toRadians(origenLatitud);
        double lat2Rad = Math.toRadians(destinoLatitud);
        double deltaLat = Math.toRadians(destinoLatitud - origenLatitud);
        double deltaLon = Math.toRadians(destinoLongitud - origenLongitud);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanciaKm = radioTierraKm * c;

        double velocidadPromedioKmH = 60.0; // heurística básica para estimación inicial
        return distanciaKm / velocidadPromedioKmH;
    }
}
