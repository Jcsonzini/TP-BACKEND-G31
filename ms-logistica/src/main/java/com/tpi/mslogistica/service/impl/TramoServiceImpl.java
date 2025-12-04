package com.tpi.mslogistica.service.impl;

import com.tpi.mslogistica.client.CatalogoClient;
import com.tpi.mslogistica.client.SolicitudesClient;
import com.tpi.mslogistica.client.dto.CamionRemotoDTO;
import com.tpi.mslogistica.client.dto.ContenedorRemotoDTO;
import com.tpi.mslogistica.client.dto.FinalizarOperacionRequest;
import com.tpi.mslogistica.client.dto.SolicitudRemotaDTO;
import com.tpi.mslogistica.domain.EstadoRuta;
import com.tpi.mslogistica.domain.EstadoTramo;
import com.tpi.mslogistica.domain.Ruta;
import com.tpi.mslogistica.domain.Tramo;
import com.tpi.mslogistica.domain.TipoTramo;
import com.tpi.mslogistica.dto.AsignarCamionTramoRequest;
import com.tpi.mslogistica.dto.CambioEstadoTramoRequest;
import com.tpi.mslogistica.dto.TramoDTO;
import com.tpi.mslogistica.repository.RutaRepository;
import com.tpi.mslogistica.repository.TramoRepository;
import com.tpi.mslogistica.service.TramoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TramoServiceImpl implements TramoService {

    private final TramoRepository tramoRepository;
    private final RutaRepository rutaRepository;
    private final SolicitudesClient solicitudesClient;
    private final CatalogoClient catalogoClient;


    @Override
    public TramoDTO obtenerPorId(Long id) {
        Long tramoId = Objects.requireNonNull(id, "El id del tramo no puede ser nulo");
        Tramo tramo = tramoRepository.findById(tramoId)
            .orElseThrow(() -> new NoSuchElementException("Tramo no encontrado: " + tramoId));
        Tramo nonNullTramo = Objects.requireNonNull(tramo, "El tramo recuperado no puede ser nulo");
        return toDTO(nonNullTramo);
    }

    @Override
    public List<TramoDTO> listarPorRuta(Long rutaId) {
        Long nonNullRutaId = Objects.requireNonNull(rutaId, "El id de la ruta no puede ser nulo");
        return tramoRepository.findByRuta_IdOrderByOrdenAsc(nonNullRutaId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public TramoDTO asignarCamion(Long tramoId, AsignarCamionTramoRequest request) {

        // =============================
        // 0) Validaciones iniciales
        // =============================

        if (tramoId == null) {
            throw new IllegalArgumentException("El ID del tramo no puede ser nulo");
        }

        if (request == null || request.getCamionId() == null) {
            throw new IllegalArgumentException("Debe especificar un camionId en la solicitud");
        }

        Long camionId = request.getCamionId();

        // Buscar tramo
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new NoSuchElementException("Tramo no encontrado: " + tramoId));

        // Solo se puede asignar camión si está PENDIENTE
        if (tramo.getEstado() != EstadoTramo.PENDIENTE) {
            throw new IllegalStateException("Solo se puede asignar camión a tramos en estado PENDIENTE");
        }

        // =============================
        // 1) Obtener solicitud asociada
        // =============================
        Ruta ruta = tramo.getRuta();
        if (ruta == null || ruta.getSolicitudId() == null) {
            throw new IllegalStateException("El tramo no posee una ruta con solicitud asociada");
        }

        Long solicitudId = ruta.getSolicitudId();

        SolicitudRemotaDTO solicitud = solicitudesClient.obtenerSolicitudPorId(solicitudId);
        if (solicitud == null || solicitud.getContenedorCodigo() == null) {
            throw new IllegalStateException(
                    "No se pudo obtener el contenedor asociado a la solicitud " + solicitudId);
        }

        String contenedorCodigo = solicitud.getContenedorCodigo();

        // =============================
        // 2) Obtener camión y contenedor desde ms-catalogo
        // =============================

        CamionRemotoDTO camion = catalogoClient.obtenerCamionPorId(camionId);
        if (camion == null) {
            throw new IllegalStateException("No existe el camión con ID " + camionId);
        }

        ContenedorRemotoDTO contenedor = catalogoClient.obtenerContenedorPorCodigo(contenedorCodigo);
        if (contenedor == null) {
            throw new IllegalStateException("No existe el contenedor con código " + contenedorCodigo);
        }

        // =============================
        // 3) Validar capacidades (RN del enunciado)
        // =============================

        Double camCapKg = camion.getCapacidadKg();
        Double camVolM3 = camion.getVolumenM3();
        Double contKg   = contenedor.getCapacidadKg();
        Double contVol  = contenedor.getVolumenReal();

        if (camCapKg == null || camVolM3 == null || contKg == null || contVol == null) {
            throw new IllegalStateException("No se pueden validar capacidades: hay atributos nulos");
        }

        // Validación por peso
        if (contKg > camCapKg) {
            throw new IllegalStateException(
                    "El contenedor requiere " + contKg + " kg pero el camión solo soporta " + camCapKg + " kg");
        }

        // Validación por volumen
        if (contVol > camVolM3) {
            throw new IllegalStateException(
                    "El contenedor tiene " + contVol + " m3 pero el camión solo admite " + camVolM3 + " m3");
        }

        // Consideramos "ocupado" si tiene algún tramo EN_CURSO
        boolean camionOcupado = tramoRepository.existsByCamionIdAndEstado(camionId, EstadoTramo.EN_CURSO);
        // (si tu enum se llama INICIADO, cambialo por EstadoTramo.INICIADO)

        if (camionOcupado) {
            throw new IllegalStateException(
                    "El camión " + camionId + " está ocupado en otro tramo en curso y no puede asignarse");
        }

        // =============================
        // 4) Asignar camión al tramo
        // =============================

        tramo.setCamionId(camionId);
        tramo.setEstado(EstadoTramo.ASIGNADO_A_CAMION);

        Tramo guardado = tramoRepository.save(tramo);

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

    // ================== A) INICIAR TRAMO ==================

    @Override
    public TramoDTO iniciarTramo(Long tramoId) {

        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new NoSuchElementException("Tramo no encontrado: " + tramoId));

        // RN1: solo se puede iniciar si está ASIGNADO_A_CAMION
        if (tramo.getEstado() != EstadoTramo.ASIGNADO_A_CAMION) {
            throw new IllegalStateException("Solo se pueden iniciar tramos en estado ASIGNADO_A_CAMION");
        }

        Ruta ruta = tramo.getRuta();
        if (ruta == null || ruta.getId() == null) {
            throw new IllegalStateException("El tramo no tiene ruta asociada");
        }

        Long rutaId = ruta.getId();
        ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new NoSuchElementException("Ruta no encontrada: " + rutaId));

        // RN2: no puede haber otro tramo EN_CURSO en la misma ruta
        boolean existeEnCurso = ruta.getTramos().stream()
                .anyMatch(t -> t.getEstado() == EstadoTramo.EN_CURSO);
        if (existeEnCurso) {
            throw new IllegalStateException("No se puede iniciar el tramo porque ya hay otro EN_CURSO en la ruta");
        }

        // RN3: secuencia correcta (si no es el primero, el anterior debe estar FINALIZADO)
        if (tramo.getOrden() != null && tramo.getOrden() > 1) {
            int ordenAnterior = tramo.getOrden() - 1;

            Tramo tramoAnterior = ruta.getTramos().stream()
                    .filter(t -> Objects.equals(t.getOrden(), ordenAnterior))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No se encontró el tramo anterior en la ruta"));

            if (tramoAnterior.getEstado() != EstadoTramo.FINALIZADO) {
                throw new IllegalStateException("No se puede iniciar este tramo porque el anterior no está FINALIZADO");
            }
        }

        // RN4: debe tener camión asignado
        if (tramo.getCamionId() == null) {
            throw new IllegalStateException("No se puede iniciar un tramo sin camión asignado");
        }

        // Si es el primer tramo que se inicia, podemos marcar la ruta como EN_CURSO
        if (ruta.getEstado() != EstadoRuta.EN_CURSO && ruta.getEstado() != EstadoRuta.COMPLETADA) {
            ruta.setEstado(EstadoRuta.EN_CURSO);
        }

        // Lógica real de inicio (fechaInicioReal, estado EN_CURSO, etc.)
        iniciarTramoInterno(tramo, ruta);  // helper requiere tramo y ruta

        tramoRepository.save(tramo);
        rutaRepository.save(ruta);

        // Avisar a ms-solicitudes que la operación está en tránsito
        Long solicitudId = ruta.getSolicitudId();
        if (solicitudId != null) {
            solicitudesClient.marcarEnTransito(solicitudId);
        }

        return toDTO(tramo);
    }


    // ================== B) FINALIZAR TRAMO ==================

    @Override
    public TramoDTO finalizarTramo(Long tramoId) {
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() -> new IllegalArgumentException("Tramo no encontrado: " + tramoId));

        // RN1: solo se puede finalizar si está EN_CURSO
        if (tramo.getEstado() != EstadoTramo.EN_CURSO) {
            throw new IllegalStateException("Solo se pueden finalizar tramos en estado EN_CURSO");
        }

        Ruta ruta = tramo.getRuta();
        if (ruta == null || ruta.getId() == null) {
            throw new IllegalStateException("El tramo no tiene ruta asociada");
        }

        Long rutaId = ruta.getId();
        ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new NoSuchElementException("Ruta no encontrada: " + rutaId));

        // RN2: debe tener fechaInicioReal (no deberíamos permitir finalizar algo que nunca se inició bien)
        if (tramo.getFechaInicioReal() == null) {
            throw new IllegalStateException("No se puede finalizar un tramo sin fecha de inicio real");
        }

        // 1) Finalizar tramo (estado FINALIZADO, fechaFinReal, tiempoHorasReal, distanciaKmReal si corresponde)
        finalizarTramoInterno(tramo);

        // RN3: coherencia de fechas (por si en el futuro se cargan manual)
        if (tramo.getFechaFinReal() != null &&
            tramo.getFechaFinReal().isBefore(tramo.getFechaInicioReal())) {
            throw new IllegalStateException("La fecha de fin real no puede ser anterior a la fecha de inicio real");
        }

        // 2) Recalcular totales de la ruta (devuelve true si todos los tramos quedaron FINALIZADOS)
        boolean rutaCompletada = recalcularTotalesRuta(ruta);

        // 3) Si la ruta no está COMPLETADA pero estaba PLANIFICADA, podemos asegurar que quede EN_CURSO
        if (!rutaCompletada && ruta.getEstado() != EstadoRuta.EN_CURSO) {
            ruta.setEstado(EstadoRuta.EN_CURSO);
        }

        tramoRepository.save(tramo);
        rutaRepository.save(ruta);

        Long solicitudId = ruta.getSolicitudId();

        // 4) Si la ruta quedó COMPLETADA, avisar a ms-solicitudes
        if (rutaCompletada) {
            notificarSolicitudCompletada(ruta);
        } else if (solicitudId != null) {
            // Tramo intermedio finalizado → contenedor queda en depósito
            solicitudesClient.marcarEnDeposito(solicitudId);
        }

        return toDTO(tramo);
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
            if (horas < 0) horas = 0.0;
            tramo.setTiempoHorasReal(horas);
        }

        // horasEsperaDepositoReal la podés calcular más adelante en base a tu modelo de depósitos
        // NOTA: acá NO se llama a setCostoReal porque el tramo no tiene ese campo
    }

    private void notificarSolicitudCompletada(Ruta ruta) {

        Long solicitudId = ruta.getSolicitudId(); // ajustá al nombre real del campo
        if (solicitudId == null) {
            throw new IllegalStateException(
                    "La ruta " + ruta.getId() + " no tiene solicitudId asociado, no se puede notificar"
            );
        }

        FinalizarOperacionRequest request = new FinalizarOperacionRequest();
        request.setRutaId(ruta.getId());
        request.setTiempoRealHoras(ruta.getTiempoTotalHorasReal());
        request.setCostoTotalReal(ruta.getCostoTotalReal());

        solicitudesClient.finalizarOperacion(solicitudId, request);
    }


    /**
     * Recalcula la distancia, tiempo y costo reales de toda la ruta a partir de los tramos.
     * El costo real incluye: costo de traslado (km × costoBaseKm) + costo de estadías en depósitos.
     */
    private boolean recalcularTotalesRuta(Ruta ruta) {
        Objects.requireNonNull(ruta, "La ruta asociada no puede ser nula");
        Objects.requireNonNull(ruta.getId(), "El id de la ruta no puede ser nulo");

        double distanciaRealTotal = 0.0;
        double tiempoRealTotal = 0.0;
        double costoRealTotal = 0.0;
        boolean todosFinalizados = true;

        for (Tramo t : ruta.getTramos()) {

            if (t.getEstado() != EstadoTramo.FINALIZADO) {
                todosFinalizados = false;
            }

            // Distancia
            double distanciaKm = 0.0;
            if (t.getDistanciaKmReal() != null) {
                distanciaKm = t.getDistanciaKmReal();
                distanciaRealTotal += t.getDistanciaKmReal();
            } else if (t.getDistanciaKmEstimada() != null) {
                distanciaKm = t.getDistanciaKmEstimada();
                distanciaRealTotal += t.getDistanciaKmEstimada();
            }

            // Tiempo
            if (t.getTiempoHorasReal() != null) {
                tiempoRealTotal += t.getTiempoHorasReal();
            } else if (t.getTiempoHorasEstimada() != null) {
                tiempoRealTotal += t.getTiempoHorasEstimada();
            }

            // Costo del tramo: traslado + estadía en depósito (si aplica)
            double costoTramoReal = calcularCostoTramoReal(t, ruta, distanciaKm);
            costoRealTotal += costoTramoReal;
        }

        ruta.setDistanciaTotalKmReal(distanciaRealTotal);
        ruta.setTiempoTotalHorasReal(tiempoRealTotal);
        ruta.setCostoTotalReal(costoRealTotal);

        if (todosFinalizados) {
            ruta.setEstado(EstadoRuta.COMPLETADA); // ajustá con tu enum/propiedad real
        }

        return todosFinalizados;
    }

    /**
     * Calcula el costo real de un tramo:
     * = Costo de traslado (distancia × costoBaseKm del camión)
     *   + Costo de estadía en depósito de origen (si es un depósito intermedio)
     *
     * NOTA: Para este MVP se usa costoBase = 150 $/km (dummy).
     * En producción, consultar ms-catalogo para obtener los valores reales del camión.
     */
    private double calcularCostoTramoReal(Tramo tramo, Ruta ruta, double distanciaKm) {
        double costoTraslado = 0.0;
        double costoEstadia = 0.0;

        // 1) Costo de traslado (distancia × costoBaseKm del camión)
        // TODO: Consultar CatalogoClient con tramo.camionId para obtener costoBaseKm real
        double costoBaseKmCamion = 150.0; // dummy: $150/km
        costoTraslado = distanciaKm * costoBaseKmCamion;

        // 2) Costo de estadía en depósito de origen (si no es ni origen ni destino de ruta)
        // Un tramo sale de un depósito intermedio si:
        //   - No es el primer tramo (orden > 1)
        //   - La descripción del origen contiene "depósito"
        //   - Hay tiempo real de espera registrado
        if (tramo.getOrden() != null && tramo.getOrden() > 1
                && esDeposito(tramo.getOrigenDescripcion())
                && tramo.getHorasEsperaDepositoReal() != null
                && tramo.getHorasEsperaDepositoReal() > 0) {

            // TODO: Consultar DepositoRepository por nombre/descripción para obtener costoEstadiaDiaria real
            // Por ahora usamos un valor dummy
            double costoEstadiaDiaria = 500.0; // dummy: $500/día
            double diasEstadia = tramo.getHorasEsperaDepositoReal() / 24.0;
            costoEstadia = diasEstadia * costoEstadiaDiaria;
        }

        return costoTraslado + costoEstadia;
    }




    private void iniciarTramoInterno(Tramo tramo, Ruta ruta) {
        tramo.setEstado(EstadoTramo.EN_CURSO);
        LocalDateTime ahora = LocalDateTime.now();
        tramo.setFechaInicioReal(ahora);

        // Cálculo de estadía real en depósito (si aplica)
        // Regla: si este tramo SALE de un depósito, la estadía es el tiempo
        // entre la llegada real previa y este inicio.
        Tramo tramoAnterior = buscarTramoAnterior(ruta, tramo);

        if (tramoAnterior != null
                && tramoAnterior.getFechaFinReal() != null
                && esDeposito(tramo.getOrigenDescripcion())) {

            Duration diff = Duration.between(tramoAnterior.getFechaFinReal(), ahora);
            double horas = diff.toMinutes() / 60.0;

            if (horas < 0) horas = 0.0;

            tramo.setHorasEsperaDepositoReal(horas);
        } else {
            tramo.setHorasEsperaDepositoReal(0.0);
        }
    }

    private void finalizarTramoInterno(Tramo tramo) {
        tramo.setEstado(EstadoTramo.FINALIZADO);
        LocalDateTime ahora = LocalDateTime.now();
        tramo.setFechaFinReal(ahora);

        if (tramo.getFechaInicioReal() != null) {
            Duration d = Duration.between(tramo.getFechaInicioReal(), tramo.getFechaFinReal());
            double horas = d.toMinutes() / 60.0;
            if (horas < 0) horas = 0.0;
            tramo.setTiempoHorasReal(horas);
        }

        // Por ahora usamos la misma distancia que la estimada; cuando tengamos datos reales reemplazamos esto.
        tramo.setDistanciaKmReal(tramo.getDistanciaKmEstimada());

        // Calcular costo real del tramo
        if (tramo.getDistanciaKmReal() != null) {
            double distancia = tramo.getDistanciaKmReal();
            double costoBaseKmCamion = 150.0; // dummy: $150/km
            double costoTraslado = distancia * costoBaseKmCamion;
            
            // Considerar costo de estadía en depósito si aplica
            double costoEstadia = 0.0;
            if (tramo.getHorasEsperaDepositoReal() != null && tramo.getHorasEsperaDepositoReal() > 0) {
                double costoEstadiaDiaria = 500.0; // dummy: $500/día
                double diasEstadia = tramo.getHorasEsperaDepositoReal() / 24.0;
                costoEstadia = diasEstadia * costoEstadiaDiaria;
            }
            
            tramo.setCostoReal(costoTraslado + costoEstadia);
        }
    }

    private Tramo buscarTramoAnterior(Ruta ruta, Tramo tramoActual) {
        if (ruta.getTramos() == null || ruta.getTramos().isEmpty()) {
            return null;
        }
        return ruta.getTramos().stream()
                .filter(t -> t.getOrden() != null
                        && tramoActual.getOrden() != null
                        && t.getOrden() == tramoActual.getOrden() - 1)
                .findFirst()
                .orElse(null);
    }

    /**
     * Heurística simple: asumimos que las descripciones de depósitos
     * contienen la palabra "Depósito" o algo similar.
     * Ajustá esto si tenés un modelo más rico (por ID de depósito, etc.).
     */
    private boolean esDeposito(String descripcion) {
        if (descripcion == null) return false;
        String lower = descripcion.toLowerCase();
        return lower.contains("depósito") || lower.contains("deposito");
    }

    // ================== MAPPER ==================

    private TramoDTO toDTO(Tramo tramo) {
        TramoDTO dto = new TramoDTO();
        dto.setId(tramo.getId());
        dto.setOrden(tramo.getOrden());
        
        // Agregar tipo
        if (tramo.getTipo() != null) {
            dto.setTipo(tramo.getTipo().name());
        }

        dto.setOrigenDescripcion(tramo.getOrigenDescripcion());
        dto.setOrigenLatitud(truncarDecimales(tramo.getOrigenLatitud(), 3));
        dto.setOrigenLongitud(truncarDecimales(tramo.getOrigenLongitud(), 3));

        dto.setDestinoDescripcion(tramo.getDestinoDescripcion());
        dto.setDestinoLatitud(truncarDecimales(tramo.getDestinoLatitud(), 3));
        dto.setDestinoLongitud(truncarDecimales(tramo.getDestinoLongitud(), 3));

        dto.setDistanciaKmEstimada(formatearDistancia(tramo.getDistanciaKmEstimada()));
        dto.setTiempoHorasEstimada(formatearTiempo(tramo.getTiempoHorasEstimada()));
        dto.setHorasEsperaDepositoEstimada(formatearTiempo(tramo.getHorasEsperaDepositoEstimada()));
        dto.setCostoAproximado(formatearCosto(tramo.getCostoAproximado()));

        dto.setDistanciaKmReal(formatearDistancia(tramo.getDistanciaKmReal()));
        dto.setTiempoHorasReal(formatearTiempo(tramo.getTiempoHorasReal()));
        dto.setHorasEsperaDepositoReal(formatearTiempo(tramo.getHorasEsperaDepositoReal()));
        dto.setCostoReal(formatearCosto(tramo.getCostoReal()));

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

    /**
     * Trunca un valor Double a un número específico de decimales.
     * La truncación es hacia abajo (floor), no redondeo.
     */
    private Double truncarDecimales(Double valor, int decimales) {
        if (valor == null) {
            return null;
        }
        double escala = Math.pow(10, decimales);
        return Math.floor(valor * escala) / escala;
    }
}
