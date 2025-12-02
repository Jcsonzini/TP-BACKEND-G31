package com.tpi.mslogistica.service.impl;

import com.tpi.mslogistica.domain.*;
import com.tpi.mslogistica.dto.RutaCreateRequest;
import com.tpi.mslogistica.dto.RutaDTO;
import com.tpi.mslogistica.dto.TramoDTO;
import com.tpi.mslogistica.repository.DepositoRepository;
import com.tpi.mslogistica.repository.RutaRepository;
import com.tpi.mslogistica.repository.TramoRepository;
import com.tpi.mslogistica.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RutaServiceImpl implements RutaService {

    private final RutaRepository rutaRepository;
    private final TramoRepository tramoRepository;
    private final DepositoRepository depositoRepository;

    // -------------------------------------------------------------
    // 1) CREAR RUTA NORMAL (ya lo tenías)
    // -------------------------------------------------------------
    @Override
    public RutaDTO crearRuta(RutaCreateRequest request) {

        Ruta ruta = new Ruta();
        ruta.setSolicitudId(request.getSolicitudId());
        ruta.setOrigenDireccion(request.getOrigenDireccion());
        ruta.setOrigenLatitud(request.getOrigenLatitud());
        ruta.setOrigenLongitud(request.getOrigenLongitud());
        ruta.setDestinoDireccion(request.getDestinoDireccion());
        ruta.setDestinoLatitud(request.getDestinoLatitud());
        ruta.setDestinoLongitud(request.getDestinoLongitud());
        ruta.setEstado(EstadoRuta.PLANIFICADA);

        // Dummy temporal
        double distanciaKm = 100.0;
        double tiempoHoras = 2.5;
        double costo = 1000.0;

        ruta.setDistanciaTotalKmEstimada(distanciaKm);
        ruta.setTiempoTotalHorasEstimada(tiempoHoras);
        ruta.setCostoTotalEstimado(costo);

        ruta.setTramos(new ArrayList<>());
        Ruta rutaGuardada = rutaRepository.save(ruta);

        // 1 tramo simple
        Tramo tramo = new Tramo();
        tramo.setRuta(rutaGuardada);
        tramo.setOrden(1);
        tramo.setOrigenDescripcion(request.getOrigenDireccion());
        tramo.setOrigenLatitud(request.getOrigenLatitud());
        tramo.setOrigenLongitud(request.getOrigenLongitud());
        tramo.setDestinoDescripcion(request.getDestinoDireccion());
        tramo.setDestinoLatitud(request.getDestinoLatitud());
        tramo.setDestinoLongitud(request.getDestinoLongitud());
        tramo.setDistanciaKmEstimada(distanciaKm);
        tramo.setTiempoHorasEstimada(tiempoHoras);
        tramo.setHorasEsperaDepositoEstimada(0.0);
        tramo.setEstado(EstadoTramo.PENDIENTE);

        tramoRepository.save(tramo);
        rutaGuardada.getTramos().add(tramo);

        return toRutaDTO(rutaGuardada);
    }

    // -------------------------------------------------------------
    // 2) OBTENER RUTA
    // -------------------------------------------------------------
    @Override
    public RutaDTO obtenerPorId(Long id) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ruta no encontrada " + id));
        return toRutaDTO(ruta);
    }

    // -------------------------------------------------------------
    // 3) LISTAR TODAS
    // -------------------------------------------------------------
    @Override
    public List<RutaDTO> listar() {
        return rutaRepository.findAll()
                .stream()
                .map(this::toRutaDTO)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------
    // 4) LISTAR POR SOLICITUD
    // -------------------------------------------------------------
    @Override
    public List<RutaDTO> listarPorSolicitud(Long solicitudId) {
        return rutaRepository.findBySolicitudId(solicitudId)
                .stream()
                .map(this::toRutaDTO)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------
    // 5) GENERAR RUTAS TENTATIVAS (entrada desde ms-solicitudes)
    // -------------------------------------------------------------
    @Override
    public List<RutaDTO> generarRutasTentativas(RutaCreateRequest request, int cantidad) {

        // Por ahora, ignoramos "cantidad". Siempre devolvemos 3.
        return generarRutasTentativas(
            request.getSolicitudId(),
                request.getOrigenDireccion(),
                request.getOrigenLatitud(),
                request.getOrigenLongitud(),
                request.getDestinoDireccion(),
                request.getDestinoLatitud(),
                request.getDestinoLongitud()
        );
    }

    // -------------------------------------------------------------
    // 6) GENERAR RUTAS TENTATIVAS (lógica real con depósitos)
    // -------------------------------------------------------------
    @Override
    public List<RutaDTO> generarRutasTentativas(Long solicitudId,
                                                String origenDireccion,
                                                double origenLat,
                                                double origenLon,
                                                String destinoDireccion,
                                                double destinoLat,
                                                double destinoLon) {

        List<Deposito> depositos = depositoRepository.findAll();

        if (depositos.isEmpty()) {
            throw new IllegalStateException("No hay depósitos cargados en la BD");
        }

        // Ordenar depósitos por cercanía al origen
        depositos.sort(Comparator.comparingDouble(
                d -> distanciaHaversine(
                        origenLat, origenLon,
                        d.getLatitud().doubleValue(),
                        d.getLongitud().doubleValue()
                )
        ));

        Deposito dep1 = depositos.get(0);
        Deposito dep2 = depositos.size() > 1 ? depositos.get(1) : dep1;
        Deposito dep3 = depositos.size() > 2 ? depositos.get(2) : dep1;

        List<RutaDTO> rutas = new ArrayList<>();

        // ---------------------------------------------------------
        // RUTA 1: ORIGEN -> DEP1 -> DESTINO
        // ---------------------------------------------------------
        rutas.add( crearRutaTentativaPersistida(
            solicitudId,
            origenDireccion, origenLat, origenLon,
                dep1,
                destinoDireccion, destinoLat, destinoLon
        ));

        // ---------------------------------------------------------
        // RUTA 2: ORIGEN -> DEP2 -> DEP3 -> DESTINO
        // ---------------------------------------------------------
        rutas.add( crearRutaTentativaPersistida(
            solicitudId,
            origenDireccion, origenLat, origenLon,
                dep2, dep3,
                destinoDireccion, destinoLat, destinoLon
        ));

        // ---------------------------------------------------------
        // RUTA 3: ORIGEN -> DEP3 -> DESTINO
        // ---------------------------------------------------------
        rutas.add( crearRutaTentativaPersistida(
            solicitudId,
            origenDireccion, origenLat, origenLon,
                dep3,
                destinoDireccion, destinoLat, destinoLon
        ));

        return rutas;
    }

    // -------------------------------------------------------------
    // CREA LA RUTA EN BD Y SUS TRAMOS (1 depósito)
    // -------------------------------------------------------------
        private RutaDTO crearRutaTentativaPersistida(Long solicitudId,
                             String origenDesc, double oLat, double oLon,
                                                 Deposito dep,
                                                 String destinoDesc, double dLat, double dLon) {

        return crearRutaTentativaPersistida(solicitudId,
            origenDesc, oLat, oLon,
                new Deposito[]{dep},
                destinoDesc, dLat, dLon);
    }

    // -------------------------------------------------------------
    // CREA LA RUTA EN BD Y SUS TRAMOS (2 depósitos)
    // -------------------------------------------------------------
        private RutaDTO crearRutaTentativaPersistida(Long solicitudId,
                             String origenDesc, double oLat, double oLon,
                                                 Deposito dep1, Deposito dep2,
                                                 String destinoDesc, double dLat, double dLon) {

        return crearRutaTentativaPersistida(solicitudId,
            origenDesc, oLat, oLon,
                new Deposito[]{dep1, dep2},
                destinoDesc, dLat, dLon);
    }

    // -------------------------------------------------------------
    // IMPLEMENTACIÓN GENÉRICA PARA 1..N DEPÓSITOS
    // -------------------------------------------------------------
    private RutaDTO crearRutaTentativaPersistida(Long solicitudId,
                                                 String origenDesc, double oLat, double oLon,
                                                 Deposito[] depositos,
                                                 String destinoDesc, double dLat, double dLon) {

        Ruta ruta = new Ruta();
        ruta.setEstado(EstadoRuta.TENTATIVA);
        ruta.setSolicitudId(solicitudId);
        ruta.setOrigenDireccion(origenDesc);
        ruta.setOrigenLatitud(oLat);
        ruta.setOrigenLongitud(oLon);
        ruta.setDestinoDireccion(destinoDesc);
        ruta.setDestinoLatitud(dLat);
        ruta.setDestinoLongitud(dLon);
        ruta.setTramos(new ArrayList<>());

        ruta = rutaRepository.save(ruta);

        List<Punto> puntos = new ArrayList<>();
        puntos.add(new Punto(origenDesc, oLat, oLon));

        for (Deposito dep : depositos) {
            puntos.add(new Punto(
                    dep.getNombre(),
                    dep.getLatitud().doubleValue(),
                    dep.getLongitud().doubleValue()
            ));
        }

        puntos.add(new Punto(destinoDesc, dLat, dLon));

        double totalDist = 0.0;
        double totalTiempo = 0.0;
        double totalCosto = 0.0;

        for (int i = 0; i < puntos.size() - 1; i++) {

            Punto a = puntos.get(i);
            Punto b = puntos.get(i + 1);

            double dist = distanciaHaversine(a.lat, a.lon, b.lat, b.lon);
            double horas = dist / 60.0; // 60 km/h promedio
            double costo = dist * 150.0;

            totalDist += dist;
            totalTiempo += horas;
            totalCosto += costo;

            Tramo tramo = new Tramo();
            tramo.setRuta(ruta);
            tramo.setOrden(i + 1);
            tramo.setOrigenDescripcion(a.desc);
            tramo.setOrigenLatitud(a.lat);
            tramo.setOrigenLongitud(a.lon);
            tramo.setDestinoDescripcion(b.desc);
            tramo.setDestinoLatitud(b.lat);
            tramo.setDestinoLongitud(b.lon);
            tramo.setDistanciaKmEstimada(dist);
            tramo.setTiempoHorasEstimada(horas);
            tramo.setHorasEsperaDepositoEstimada(0.0);
            tramo.setEstado(EstadoTramo.PENDIENTE);

            tramoRepository.save(tramo);
            ruta.getTramos().add(tramo);
        }

        ruta.setDistanciaTotalKmEstimada(totalDist);
        ruta.setTiempoTotalHorasEstimada(totalTiempo);
        ruta.setCostoTotalEstimado(totalCosto);

        ruta = rutaRepository.save(ruta);

        return toRutaDTO(ruta);
    }

    // -------------------------------------------------------------
    // PUNTO (clase interna)
    // -------------------------------------------------------------
    private static class Punto {
        String desc;
        double lat;
        double lon;

        Punto(String desc, double lat, double lon) {
            this.desc = desc;
            this.lat = lat;
            this.lon = lon;
        }
    }

    // -------------------------------------------------------------
    // HAVERSINE
    // -------------------------------------------------------------
    private double distanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    // -------------------------------------------------------------
    // MAPPERS — no se tocó nada
    // -------------------------------------------------------------
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

        if (ruta.getEstado() != null) dto.setEstado(ruta.getEstado().name());
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

        if (tramo.getEstado() != null) dto.setEstado(tramo.getEstado().name());

        return dto;
    }
}
