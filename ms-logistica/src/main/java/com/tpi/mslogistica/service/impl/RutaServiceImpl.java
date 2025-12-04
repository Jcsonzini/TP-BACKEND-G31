package com.tpi.mslogistica.service.impl;

import com.tpi.mslogistica.client.OsrmClient;
import com.tpi.mslogistica.domain.Deposito;
import com.tpi.mslogistica.domain.EstadoRuta;
import com.tpi.mslogistica.domain.EstadoTramo;
import com.tpi.mslogistica.domain.Ruta;
import com.tpi.mslogistica.domain.Tramo;
import com.tpi.mslogistica.dto.RutaCreateRequest;
import com.tpi.mslogistica.dto.RutaDTO;
import com.tpi.mslogistica.dto.TramoDTO;
import com.tpi.mslogistica.repository.DepositoRepository;
import com.tpi.mslogistica.repository.RutaRepository;
import com.tpi.mslogistica.repository.TramoRepository;
import com.tpi.mslogistica.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class RutaServiceImpl implements RutaService {

    private final RutaRepository rutaRepository;
    private final TramoRepository tramoRepository;
    private final DepositoRepository depositoRepository;
    private final OsrmClient osrmClient;

    // =========================================================
    // 1) CREAR RUTA "NORMAL" (ORIGEN → DESTINO directo)
    // =========================================================
    @Override
    public RutaDTO crearRuta(RutaCreateRequest request) {

        Objects.requireNonNull(request, "El request de ruta no puede ser nulo");

        Ruta ruta = new Ruta();
        ruta.setSolicitudId(request.getSolicitudId());

        ruta.setOrigenDireccion(request.getOrigenDireccion());
        ruta.setOrigenLatitud(request.getOrigenLatitud());
        ruta.setOrigenLongitud(request.getOrigenLongitud());

        ruta.setDestinoDireccion(request.getDestinoDireccion());
        ruta.setDestinoLatitud(request.getDestinoLatitud());
        ruta.setDestinoLongitud(request.getDestinoLongitud());

        // Llamamos a OSRM para obtener distancia y tiempo reales para el tramo directo
        OsrmClient.OsrmRoute osrmRoute = osrmClient.route(
                request.getOrigenLatitud(),
                request.getOrigenLongitud(),
                request.getDestinoLatitud(),
                request.getDestinoLongitud()
        );

        double distanciaKm = osrmRoute.getDistance() / 1000.0;
        double tiempoHoras = osrmRoute.getDuration() / 3600.0;
        
        // El costoEstimado se calcula usando los parámetros de la tarifa
        double costoBaseKm = request.getCostoBaseKm() != null ? request.getCostoBaseKm() : 150.0;
        double costoEstimado = distanciaKm * costoBaseKm;

        ruta.setDistanciaTotalKmEstimada(distanciaKm);
        ruta.setTiempoTotalHorasEstimada(tiempoHoras);
        ruta.setCostoTotalEstimado(costoEstimado);
        ruta.setEstado(EstadoRuta.TENTATIVA); // se confirma recién al seleccionar
        ruta.setTramos(new ArrayList<>());

        Ruta rutaGuardada = rutaRepository.save(ruta);

        Tramo tramo = new Tramo();
        tramo.setRuta(rutaGuardada);
        tramo.setOrden(1);
        tramo.setTipo(com.tpi.mslogistica.domain.TipoTramo.ORIGEN);

        tramo.setOrigenDescripcion(request.getOrigenDireccion());
        tramo.setOrigenLatitud(request.getOrigenLatitud());
        tramo.setOrigenLongitud(request.getOrigenLongitud());

        tramo.setDestinoDescripcion(request.getDestinoDireccion());
        tramo.setDestinoLatitud(request.getDestinoLatitud());
        tramo.setDestinoLongitud(request.getDestinoLongitud());

        tramo.setDistanciaKmEstimada(distanciaKm);
        tramo.setTiempoHorasEstimada(tiempoHoras);
        tramo.setCostoAproximado(costoEstimado);
        tramo.setHorasEsperaDepositoEstimada(0.0); // Sin espera en origen
        tramo.setEstado(EstadoTramo.PENDIENTE);

        tramoRepository.save(tramo);

        rutaGuardada.getTramos().add(tramo);

        return toRutaDTO(rutaGuardada);
    }

    // =========================================================
    // 2) OBTENER / LISTAR
    // =========================================================
    @Override
    public RutaDTO obtenerPorId(Long id) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ruta no encontrada con id " + id));
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
        return rutaRepository.findBySolicitudId(solicitudId)
                .stream()
                .map(this::toRutaDTO)
                .collect(Collectors.toList());
    }

    // =========================================================
    // 3) GENERAR RUTAS TENTATIVAS CON DEPÓSITOS + OSRM
    //    (versión "nueva" con request - usada por ms-solicitudes)
    // =========================================================
    @Override
    public List<RutaDTO> generarRutasTentativas(RutaCreateRequest request, int cantidad) {

        Objects.requireNonNull(request, "El request de ruta no puede ser nulo");

        double origenLat = request.getOrigenLatitud();
        double origenLon = request.getOrigenLongitud();
        double destinoLat = request.getDestinoLatitud();
        double destinoLon = request.getDestinoLongitud();
        int objetivo = Math.max(cantidad, 4);

        // Traemos todos los depósitos
        List<Deposito> depositos = depositoRepository.findAll();

        // Ordenamos por cercanía al segmento origen-destino usando Haversine como aproximación
        List<Deposito> cercanos = new ArrayList<>(depositos);
        cercanos.sort(Comparator.comparingDouble(dep ->
            distanciaHaversine(
                origenLat,
                origenLon,
                dep.getLatitud().doubleValue(),
                dep.getLongitud().doubleValue()
            )
        ));

        List<RutaDTO> resultado = new ArrayList<>();

        // Ruta 1: ORIGEN → DESTINO (sin depósitos)
        if (resultado.size() < objetivo) {
            RutaDTO r0 = crearRutaTentativaPersistida(request, Collections.emptyList());
            resultado.add(r0);
        }

        // Ruta 2: ORIGEN → depósito más cercano → DESTINO
        if (!cercanos.isEmpty() && resultado.size() < objetivo) {
            RutaDTO r1 = crearRutaTentativaPersistida(request, List.of(cercanos.get(0)));
            resultado.add(r1);
        }

        // Ruta 3: ORIGEN → depósito segundo → DESTINO
        if (cercanos.size() > 1 && resultado.size() < objetivo) {
            RutaDTO r2 = crearRutaTentativaPersistida(request, List.of(cercanos.get(1)));
            resultado.add(r2);
        }

        // Ruta 4: ORIGEN → dep1 → dep2 → DESTINO (si hay al menos 2 depósitos)
        if (cercanos.size() > 1 && resultado.size() < objetivo) {
            RutaDTO r3 = crearRutaTentativaPersistida(request, List.of(cercanos.get(0), cercanos.get(1)));
            resultado.add(r3);
        }

        // Ruta extra: ORIGEN → dep1 → dep3 → DESTINO (cuando hay al menos 3 depósitos y todavía falta)
        if (cercanos.size() > 2 && resultado.size() < objetivo) {
            RutaDTO rExtra = crearRutaTentativaPersistida(request, List.of(cercanos.get(0), cercanos.get(2)));
            resultado.add(rExtra);
        }

        // Si aún faltan rutas para llegar al objetivo, rellenamos con la última combinación disponible
        while (!resultado.isEmpty() && resultado.size() < objetivo) {
            resultado.add(resultado.get(resultado.size() - 1));
        }

        return resultado;
    }

    // =========================================================
    // 3.bis) Versión "larga" por si se usa desde otros lados
    // =========================================================
    @Override
    public List<RutaDTO> generarRutasTentativas(Long solicitudId,
                                                String origenDescripcion,
                                                double origenLat,
                                                double origenLon,
                                                String destinoDescripcion,
                                                double destinoLat,
                                                double destinoLon) {

        RutaCreateRequest request = new RutaCreateRequest();
        request.setSolicitudId(solicitudId);
        request.setOrigenDireccion(origenDescripcion);
        request.setOrigenLatitud(origenLat);
        request.setOrigenLongitud(origenLon);
        request.setDestinoDireccion(destinoDescripcion);
        request.setDestinoLatitud(destinoLat);
        request.setDestinoLongitud(destinoLon);

        // por defecto generamos 4 rutas tentativas
        return generarRutasTentativas(request, 4);
    }

    // =========================================================
    // 4) SELECCIONAR RUTA (y borrar las demás tentativas de la misma solicitud)
    // =========================================================
    @Override
    @Transactional
    public RutaDTO seleccionarRuta(Long rutaId) {
        Ruta ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() ->
                        new NoSuchElementException("Ruta no encontrada con id " + rutaId));

        Long solicitudId = ruta.getSolicitudId();
        if (solicitudId == null) {
            throw new IllegalStateException("La ruta no tiene solicitud asociada");
        }

        // Borramos TODAS las otras rutas tentativas de esa solicitud
        rutaRepository.deleteBySolicitudIdAndIdNot(solicitudId, rutaId);

        // Marcamos esta ruta como PLANIFICADA (o el estado que uses para definitiva)
        ruta.setEstado(EstadoRuta.PLANIFICADA);
        Ruta rutaGuardada = rutaRepository.save(ruta);

        // Devolvemos el DTO usando el mapper ya existente
        return toRutaDTO(rutaGuardada);
    }

    // =========================================================
    // IMPLEMENTACIÓN GENÉRICA PARA 1..N DEPÓSITOS
    // =========================================================
    private RutaDTO crearRutaTentativaPersistida(RutaCreateRequest request,
                                                 List<Deposito> depositosIntermedios) {

        // Extraer parámetros de tarifa (con valores por defecto si no vienen)
        double costoBaseKm = request.getCostoBaseKm() != null ? request.getCostoBaseKm() : 150.0;
        double costoEstadiaDiaria = request.getCostoEstadiaDiaria() != null ? request.getCostoEstadiaDiaria() : 500.0;
        double costoDescargaCarga = request.getCostoDescargaCarga() != null ? request.getCostoDescargaCarga() : 1000.0;

        String origenDesc = request.getOrigenDireccion();
        double oLat = request.getOrigenLatitud();
        double oLon = request.getOrigenLongitud();
        String destinoDesc = request.getDestinoDireccion();
        double dLat = request.getDestinoLatitud();
        double dLon = request.getDestinoLongitud();

        // 1) Crear Ruta base
        Ruta ruta = new Ruta();
        ruta.setSolicitudId(request.getSolicitudId());

        ruta.setOrigenDireccion(origenDesc);
        ruta.setOrigenLatitud(oLat);
        ruta.setOrigenLongitud(oLon);

        ruta.setDestinoDireccion(destinoDesc);
        ruta.setDestinoLatitud(dLat);
        ruta.setDestinoLongitud(dLon);

        ruta.setEstado(EstadoRuta.TENTATIVA); // permanece tentativa hasta la selección
        ruta.setTramos(new ArrayList<>());

        Ruta rutaGuardada = rutaRepository.save(ruta);

        // Construimos la secuencia de puntos: origen -> depósitos -> destino
        List<Punto> puntos = new ArrayList<>();
        puntos.add(new Punto(origenDesc, oLat, oLon));

        for (Deposito dep : depositosIntermedios) {
                puntos.add(new Punto(
                    dep.getNombre(),
                    dep.getLatitud().doubleValue(),
                    dep.getLongitud().doubleValue()
                ));
        }

        puntos.add(new Punto(destinoDesc, dLat, dLon));

        double distanciaTotal = 0.0;
        double tiempoTotal = 0.0;
        double costoTotal = 0.0;

        int orden = 1;
        for (int i = 0; i < puntos.size() - 1; i++) {
            Punto p1 = puntos.get(i);
            Punto p2 = puntos.get(i + 1);

            // Llamamos a OSRM para cada tramo
            OsrmClient.OsrmRoute osrmRoute = osrmClient.route(
                    p1.latitud,
                    p1.longitud,
                    p2.latitud,
                    p2.longitud
            );

            double distanciaKm = osrmRoute.getDistance() / 1000.0;
            double tiempoHoras = osrmRoute.getDuration() / 3600.0;
            // Costo calculado usando el costoBaseKm de la tarifa
            double costo = distanciaKm * costoBaseKm;

            distanciaTotal += distanciaKm;
            tiempoTotal += tiempoHoras;
            costoTotal += costo;

            Tramo tramo = new Tramo();
            tramo.setRuta(rutaGuardada);
            tramo.setOrden(orden);
            
            // Determinar tipo de tramo
            com.tpi.mslogistica.domain.TipoTramo tipoTramo;
            if (orden == 1) {
                tipoTramo = com.tpi.mslogistica.domain.TipoTramo.ORIGEN;
            } else if (orden == puntos.size() - 1) {
                tipoTramo = com.tpi.mslogistica.domain.TipoTramo.DESTINO;
            } else {
                tipoTramo = com.tpi.mslogistica.domain.TipoTramo.INTERMEDIO;
            }
            tramo.setTipo(tipoTramo);
            orden++;

            tramo.setOrigenDescripcion(p1.descripcion);
            tramo.setOrigenLatitud(p1.latitud);
            tramo.setOrigenLongitud(p1.longitud);

            tramo.setDestinoDescripcion(p2.descripcion);
            tramo.setDestinoLatitud(p2.latitud);
            tramo.setDestinoLongitud(p2.longitud);

            tramo.setDistanciaKmEstimada(distanciaKm);
            tramo.setTiempoHorasEstimada(tiempoHoras);
            
            // Calcular horas de espera estimadas y costo de estadía para depósitos intermedios
            if (tipoTramo == com.tpi.mslogistica.domain.TipoTramo.INTERMEDIO) {
                // Estimación: entre 4 y 10 horas de espera en depósito intermedio
                double horasEsperaEstimada = 4.0 + Math.random() * 6.0; // Random entre 4 y 10
                tramo.setHorasEsperaDepositoEstimada(horasEsperaEstimada);
                
                // Agregar costo de estadía usando el valor de la tarifa
                // Costo de estadía prorrateado por las horas estimadas (suponemos día = 24 horas)
                double costoEstadiaProrrateo = (costoEstadiaDiaria / 24.0) * horasEsperaEstimada;
                costo += costoEstadiaProrrateo;
                
                // También agregamos costo de descarga/carga de la tarifa
                costo += costoDescargaCarga;
                
                costoTotal += costoEstadiaProrrateo + costoDescargaCarga;
                tramo.setCostoAproximado(costo);
            } else {
                tramo.setHorasEsperaDepositoEstimada(0.0);
                tramo.setCostoAproximado(costo);
            }
            
            tramo.setEstado(EstadoTramo.PENDIENTE);

            tramoRepository.save(tramo);
            rutaGuardada.getTramos().add(tramo);
        }

        rutaGuardada.setDistanciaTotalKmEstimada(distanciaTotal);
        rutaGuardada.setTiempoTotalHorasEstimada(tiempoTotal);
        rutaGuardada.setCostoTotalEstimado(costoTotal);

        rutaGuardada = rutaRepository.save(rutaGuardada);

        return toRutaDTO(rutaGuardada);
    }

    // Pequeña clase interna para manejar puntos del recorrido
    private static class Punto {
        String descripcion;
        double latitud;
        double longitud;

        Punto(String descripcion, double latitud, double longitud) {
            this.descripcion = descripcion;
            this.latitud = latitud;
            this.longitud = longitud;
        }
    }

    // =========================================================
    // Haversine para ordenar depósitos
    // =========================================================
    private double distanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // radio aproximado de la Tierra en km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return R * 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    // =========================================================
    // MAPPERS
    // =========================================================
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

        dto.setDistanciaTotalKmEstimada(formatearDistancia(ruta.getDistanciaTotalKmEstimada()));
        dto.setTiempoTotalHorasEstimada(formatearTiempo(ruta.getTiempoTotalHorasEstimada()));
        dto.setCostoTotalEstimado(formatearCosto(ruta.getCostoTotalEstimado()));

        dto.setDistanciaTotalKmReal(formatearDistancia(ruta.getDistanciaTotalKmReal()));
        dto.setTiempoTotalHorasReal(formatearTiempo(ruta.getTiempoTotalHorasReal()));
        dto.setCostoTotalReal(formatearCosto(ruta.getCostoTotalReal()));

        if (ruta.getEstado() != null) {
            dto.setEstado(ruta.getEstado().name());
        }

        if (ruta.getTramos() != null) {
            dto.setTramos(
                    ruta.getTramos().stream()
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

        dto.setDistanciaKmEstimada(formatearDistancia(tramo.getDistanciaKmEstimada()));
        dto.setTiempoHorasEstimada(formatearTiempo(tramo.getTiempoHorasEstimada()));
        dto.setHorasEsperaDepositoEstimada(formatearTiempo(tramo.getHorasEsperaDepositoEstimada()));

        dto.setDistanciaKmReal(formatearDistancia(tramo.getDistanciaKmReal()));
        dto.setTiempoHorasReal(formatearTiempo(tramo.getTiempoHorasReal()));
        dto.setHorasEsperaDepositoReal(formatearTiempo(tramo.getHorasEsperaDepositoReal()));

        dto.setCamionId(tramo.getCamionId());
        dto.setFechaInicioReal(tramo.getFechaInicioReal());
        dto.setFechaFinReal(tramo.getFechaFinReal());

        if (tramo.getEstado() != null) {
            dto.setEstado(tramo.getEstado().name());
        }

        return dto;
    }

    /**
     * Formatea distancia a 3 decimales con unidad "km"
     */
    private String formatearDistancia(Double valor) {
        if (valor == null) {
            return null;
        }
        return String.format("%.3f km", valor);
    }

    /**
     * Formatea tiempo a 3 decimales con unidad "h"
     */
    private String formatearTiempo(Double valor) {
        if (valor == null) {
            return null;
        }
        return String.format("%.3f h", valor);
    }

    /**
     * Formatea costo con símbolo $ al inicio
     */
    private String formatearCosto(Double valor) {
        if (valor == null) {
            return null;
        }
        return String.format("$%.2f", valor);
    }
}
