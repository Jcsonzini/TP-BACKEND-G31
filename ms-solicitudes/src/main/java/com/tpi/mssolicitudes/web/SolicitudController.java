package com.tpi.mssolicitudes.web;

import com.tpi.mssolicitudes.client.dto.FinalizarOperacionRequest;
import com.tpi.mssolicitudes.client.dto.RutaDTO;
import com.tpi.mssolicitudes.dto.CambioEstadoSolicitudRequest;
import com.tpi.mssolicitudes.dto.SolicitudCreateRequest;
import com.tpi.mssolicitudes.dto.SolicitudDTO;
import com.tpi.mssolicitudes.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@Tag(name = "Solicitudes", description = "Gestión de solicitudes de transporte")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva solicitud de transporte")
    public ResponseEntity<SolicitudDTO> crear(@RequestBody SolicitudCreateRequest request) {
        SolicitudDTO creada = solicitudService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una solicitud por ID")
    public ResponseEntity<SolicitudDTO> actualizar(@PathVariable Long id,
                                                   @RequestBody SolicitudDTO dto) {
        SolicitudDTO actualizada = solicitudService.actualizar(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una solicitud")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        solicitudService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Cambiar el estado de una solicitud")
    public ResponseEntity<SolicitudDTO> cambiarEstado(@PathVariable Long id,
                                                      @RequestBody CambioEstadoSolicitudRequest request) {
        SolicitudDTO actualizada = solicitudService.cambiarEstado(id, request);
        return ResponseEntity.ok(actualizada);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una solicitud por ID")
    public ResponseEntity<SolicitudDTO> obtenerPorId(@PathVariable Long id) {
        SolicitudDTO dto = solicitudService.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/por-cliente/{clienteId}")
    @Operation(summary = "Obtener solicitudes asociadas a un cliente")
    public ResponseEntity<List<SolicitudDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<SolicitudDTO> lista = solicitudService.listarPorCliente(clienteId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping
    @Operation(summary = "Listar todas las solicitudes")
    public ResponseEntity<List<SolicitudDTO>> listar() {
        List<SolicitudDTO> lista = solicitudService.listar();
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/{id}/generar-rutas")
    @Operation(summary = "Generar rutas tentativas para una solicitud")
    public ResponseEntity<List<RutaDTO>> generarRutasParaSolicitud(@PathVariable Long id) {
        List<RutaDTO> rutas = solicitudService.generarRutasParaSolicitud(id);
        return ResponseEntity.ok(rutas);
    }

    @PostMapping("/{id}/asignar-ruta/{rutaId}")
    @Operation(summary = "Asignar una ruta definitiva a la solicitud")
    public ResponseEntity<SolicitudDTO> asignarRuta(@PathVariable Long id, @PathVariable Long rutaId) {
        SolicitudDTO actualizada = solicitudService.asignarRuta(id, rutaId);
        return ResponseEntity.ok(actualizada);
    }

    @PutMapping("/{id}/finalizar-operacion")
    @Operation(summary = "Marcar una solicitud como entregada y registrar datos finales")
    public ResponseEntity<SolicitudDTO> finalizarOperacion(@PathVariable Long id,
                                                           @RequestBody FinalizarOperacionRequest request) {
        SolicitudDTO actualizada = solicitudService.finalizarOperacion(id, request);
        return ResponseEntity.ok(actualizada);
    }


}
