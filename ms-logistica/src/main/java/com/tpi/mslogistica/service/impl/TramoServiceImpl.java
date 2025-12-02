package com.tpi.mslogistica.service.impl;

import com.tpi.mslogistica.domain.EstadoTramo;
import com.tpi.mslogistica.domain.Ruta;
import com.tpi.mslogistica.domain.Tramo;
import com.tpi.mslogistica.dto.AsignarCamionTramoRequest;
import com.tpi.mslogistica.dto.CambioEstadoTramoRequest;
import com.tpi.mslogistica.dto.TramoDTO;
import com.tpi.mslogistica.repository.RutaRepository;
import com.tpi.mslogistica.repository.TramoRepository;
import com.tpi.mslogistica.service.TramoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TramoServiceImpl implements TramoService {

    private final TramoRepository tramoRepository;
    private final RutaRepository rutaRepository;

    @Override
    public TramoDTO obtenerPorId(Long id) {
        Long tramoId = Objects.requireNonNull(id, "El id del tramo no puede ser nulo");
        Tramo tramo = tramoRepository.findById(tramoId)
            .orElseThrow(() -> new NoSuchElementException("Tramo no encontrado: " + tramoId));
        Tramo nonNullTramo = Objects.requireNonNull(tramo, "El tramo recuperado no puede ser nulo");
        return toDTO(nonNullTramo);
    }

    @Override
    public TramoDTO asignarCamion(Long tramoId, AsignarCamionTramoRequest request) {
        Long nonNullTramoId = Objects.requireNonNull(tramoId, "El id del tramo no puede ser nulo");
        AsignarCamionTramoRequest nonNullRequest = Objects.requireNonNull(request, "El request de asignación no puede ser nulo");

        Tramo tramo = tramoRepository.findById(nonNullTramoId)
            .orElseThrow(() -> new NoSuchElementException("Tramo no encontrado: " + nonNullTramoId));
        Tramo nonNullTramo = Objects.requireNonNull(tramo, "El tramo recuperado no puede ser nulo");

        nonNullTramo.setCamionId(nonNullRequest.getCamionId());
        // Más adelante: validar contra ms-catalogo que el camión exista y tenga capacidad

        Tramo guardado = tramoRepository.save(nonNullTramo);
        return toDTO(guardado);
    }

    @Override
    public TramoDTO cambiarEstado(Long tramoId, CambioEstadoTramoRequest request) {
        Long nonNullTramoId = Objects.requireNonNull(tramoId, "El id del tramo no puede ser nulo");
        CambioEstadoTramoRequest nonNullRequest = Objects.requireNonNull(request, "El request de cambio de estado no puede ser nulo");

        Tramo tramo = tramoRepository.findById(nonNullTramoId)
                .orElseThrow(() -> new NoSuchElementException("Tramo no encontrado: " + nonNullTramoId));
        Tramo nonNullTramo = Objects.requireNonNull(tramo, "El tramo recuperado no puede ser nulo");

        String accion = nonNullRequest.getAccion();
        if ("INICIAR".equalsIgnoreCase(accion)) {
            iniciarTramo(nonNullTramo);
        } else if ("FINALIZAR".equalsIgnoreCase(accion)) {
            finalizarTramo(nonNullTramo);
        } else {
            throw new IllegalArgumentException("Acción no válida para tramo: " + accion);
        }

        Tramo guardado = tramoRepository.save(nonNullTramo);

        // Recalcular totales reales de la ruta cuando se finaliza un tramo
        if ("FINALIZAR".equalsIgnoreCase(accion)) {
            Ruta ruta = guardado.getRuta();
            if (ruta != null) {
                recalcularTotalesRuta(ruta);
            }
        }

        return toDTO(guardado);
    }

    // ================== LÓGICA DE NEGOCIO ==================

    private void iniciarTramo(Tramo tramo) {
        tramo.setEstado(EstadoTramo.EN_CURSO);
        tramo.setFechaInicioReal(LocalDateTime.now());
    }

    private void finalizarTramo(Tramo tramo) {
        tramo.setEstado(EstadoTramo.FINALIZADO);
        tramo.setFechaFinReal(LocalDateTime.now());

        if (tramo.getFechaInicioReal() != null && tramo.getFechaFinReal() != null) {
            Duration dur = Duration.between(tramo.getFechaInicioReal(), tramo.getFechaFinReal());
            double horas = dur.toMinutes() / 60.0;
            tramo.setTiempoHorasReal(horas);
        }
        // horasEsperaDepositoReal la podés calcular más adelante en base a tu modelo de depósitos
    }

    /**
     * Recalcula la distancia, tiempo y costo reales de toda la ruta
     * a partir de los tramos. Por ahora sumamos lo que haya cargado.
     */
    private void recalcularTotalesRuta(Ruta ruta) {
        Ruta nonNullRuta = Objects.requireNonNull(ruta, "La ruta asociada no puede ser nula");
        Long rutaId = Objects.requireNonNull(nonNullRuta.getId(), "El id de la ruta no puede ser nulo");

        Ruta r = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new NoSuchElementException("Ruta no encontrada: " + rutaId));

        double distanciaRealTotal = 0.0;
        double tiempoRealTotal = 0.0;

        for (Tramo t : r.getTramos()) {
            if (t.getDistanciaKmReal() != null) {
                distanciaRealTotal += t.getDistanciaKmReal();
            } else if (t.getDistanciaKmEstimada() != null) {
                distanciaRealTotal += t.getDistanciaKmEstimada();
            }

            if (t.getTiempoHorasReal() != null) {
                tiempoRealTotal += t.getTiempoHorasReal();
            } else if (t.getTiempoHorasEstimada() != null) {
                tiempoRealTotal += t.getTiempoHorasEstimada();
            }
        }

        r.setDistanciaTotalKmReal(distanciaRealTotal);
        r.setTiempoTotalHorasReal(tiempoRealTotal);

        // TODO: calcular costoTotalReal usando tarifas, estadías, etc.
        // Por ahora lo dejamos nulo o igual al estimado
        r.setCostoTotalReal(r.getCostoTotalEstimado());

        rutaRepository.save(r);
    }

    // ================== MAPPER ==================

    private TramoDTO toDTO(Tramo tramo) {
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

        dto.setCamionId(tramo.getCamionId());
        dto.setFechaInicioReal(tramo.getFechaInicioReal());
        dto.setFechaFinReal(tramo.getFechaFinReal());

        if (tramo.getEstado() != null) {
            dto.setEstado(tramo.getEstado().name());
        }

        return dto;
    }
}
