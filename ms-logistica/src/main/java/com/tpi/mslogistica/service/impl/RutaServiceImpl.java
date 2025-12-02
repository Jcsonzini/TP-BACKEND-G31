package com.tpi.mslogistica.service.impl;

import com.tpi.mslogistica.domain.EstadoRuta;
import com.tpi.mslogistica.domain.EstadoTramo;
import com.tpi.mslogistica.domain.Ruta;
import com.tpi.mslogistica.domain.Tramo;
import com.tpi.mslogistica.dto.RutaCreateRequest;
import com.tpi.mslogistica.dto.RutaDTO;
import com.tpi.mslogistica.dto.TramoDTO;
import com.tpi.mslogistica.repository.RutaRepository;
import com.tpi.mslogistica.repository.TramoRepository;
import com.tpi.mslogistica.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RutaServiceImpl implements RutaService {

    private final RutaRepository rutaRepository;
    private final TramoRepository tramoRepository;

    @Override
    public RutaDTO crearRuta(RutaCreateRequest request) {

        // 1) Crear entidad Ruta base en estado PLANIFICADA
        Ruta ruta = new Ruta();
        ruta.setSolicitudId(request.getSolicitudId());

        ruta.setOrigenDireccion(request.getOrigenDireccion());
        ruta.setOrigenLatitud(request.getOrigenLatitud());
        ruta.setOrigenLongitud(request.getOrigenLongitud());

        ruta.setDestinoDireccion(request.getDestinoDireccion());
        ruta.setDestinoLatitud(request.getDestinoLatitud());
        ruta.setDestinoLongitud(request.getDestinoLongitud());

        ruta.setEstado(EstadoRuta.PLANIFICADA);

        // ====== LÓGICA DE CÁLCULO PROVISORIA ======
        // TODO: reemplazar por integración real con Google Maps
        double distanciaKmEstimada = 100.0;
        double tiempoHorasEstimada = 2.5;
        double costoTotalEstimado = 1000.0;

        ruta.setDistanciaTotalKmEstimada(distanciaKmEstimada);
        ruta.setTiempoTotalHorasEstimada(tiempoHorasEstimada);
        ruta.setCostoTotalEstimado(costoTotalEstimado);

        ruta.setTramos(new ArrayList<>());

        Ruta rutaGuardada = rutaRepository.save(ruta);

        // 2) Crear un tramo único ORIGEN → DESTINO (para empezar simple)
        Tramo tramo = new Tramo();
        tramo.setRuta(rutaGuardada);
        tramo.setOrden(1);

        tramo.setOrigenDescripcion(request.getOrigenDireccion());
        tramo.setOrigenLatitud(request.getOrigenLatitud());
        tramo.setOrigenLongitud(request.getOrigenLongitud());

        tramo.setDestinoDescripcion(request.getDestinoDireccion());
        tramo.setDestinoLatitud(request.getDestinoLatitud());
        tramo.setDestinoLongitud(request.getDestinoLongitud());

        tramo.setDistanciaKmEstimada(distanciaKmEstimada);
        tramo.setTiempoHorasEstimada(tiempoHorasEstimada);
        tramo.setHorasEsperaDepositoEstimada(0.0);

        tramo.setEstado(EstadoTramo.PENDIENTE);

        Tramo tramoGuardado = tramoRepository.save(tramo);

        rutaGuardada.getTramos().add(tramoGuardado);

        return toRutaDTO(rutaGuardada);
    }

    @Override
    public RutaDTO obtenerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la ruta no puede ser nulo");
        }
        Long nonNullId = Objects.requireNonNull(id);
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ruta no encontrada con id " + nonNullId));
        return toRutaDTO(ruta);
    }

    @Override
    public List<RutaDTO> listar() {
        return rutaRepository.findAll()
                .stream()
                .map(this::toRutaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RutaDTO> listarPorSolicitud(Long solicitudId) {
        if (solicitudId == null) {
            throw new IllegalArgumentException("El id de la solicitud no puede ser nulo");
        }
        Long nonNullSolicitudId = Objects.requireNonNull(solicitudId);
        return rutaRepository.findBySolicitudId(nonNullSolicitudId)
                .stream()
                .map(this::toRutaDTO)
                .collect(Collectors.toList());
    }

    // ================== MAPPERS ==================

    private RutaDTO toRutaDTO(Ruta ruta) {
        RutaDTO dto = new RutaDTO();
        dto.setId(ruta.getId());
        dto.setSolicitudId(ruta.getSolicitudId());

        dto.setOrigenDireccion(ruta.getOrigenDireccion());
        dto.setOrigenLatitud(ruta.getOrigenLatitud());
        dto.setOrigenLongitud(ruta.getOrigenLongitud());

        dto.setDestinoDireccion(ruta.getDestinoDireccion());
        dto.setDestinoLatitud(ruta.getDestinoLatitud());
        dto.setDestinoLongitud(ruta.getDestinoLongitud());

        dto.setDistanciaTotalKmEstimada(ruta.getDistanciaTotalKmEstimada());
        dto.setTiempoTotalHorasEstimada(ruta.getTiempoTotalHorasEstimada());
        dto.setCostoTotalEstimado(ruta.getCostoTotalEstimado());

        dto.setDistanciaTotalKmReal(ruta.getDistanciaTotalKmReal());
        dto.setTiempoTotalHorasReal(ruta.getTiempoTotalHorasReal());
        dto.setCostoTotalReal(ruta.getCostoTotalReal());

        if (ruta.getEstado() != null) {
            dto.setEstado(ruta.getEstado().name());
        }

        dto.setFechaCreacion(ruta.getFechaCreacion());

        if (ruta.getTramos() != null) {
            dto.setTramos(
                    ruta.getTramos()
                            .stream()
                            .map(this::toTramoDTO)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private TramoDTO toTramoDTO(Tramo tramo) {
        TramoDTO dto = new TramoDTO();
        dto.setId(tramo.getId());
        dto.setOrden(tramo.getOrden());

        dto.setOrigenDescripcion(tramo.getOrigenDescripcion());
        dto.setOrigenLatitud(tramo.getOrigenLatitud());
        dto.setOrigenLongitud(tramo.getOrigenLongitud());

        dto.setDestinoDescripcion(tramo.getDestinoDescripcion());
        dto.setDestinoLatitud(tramo.getDestinoLatitud());
        dto.setDestinoLongitud(tramo.getDestinoLongitud());

        dto.setDistanciaKmEstimada(tramo.getDistanciaKmEstimada());
        dto.setTiempoHorasEstimada(tramo.getTiempoHorasEstimada());
        dto.setHorasEsperaDepositoEstimada(tramo.getHorasEsperaDepositoEstimada());

        dto.setDistanciaKmReal(tramo.getDistanciaKmReal());
        dto.setTiempoHorasReal(tramo.getTiempoHorasReal());
        dto.setHorasEsperaDepositoReal(tramo.getHorasEsperaDepositoReal());

        if (tramo.getEstado() != null) {
            dto.setEstado(tramo.getEstado().name());
        }

        return dto;
    }
}
