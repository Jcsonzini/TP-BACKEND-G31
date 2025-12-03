package com.tpi.mslogistica.web;

import com.tpi.mslogistica.dto.AsignarCamionTramoRequest;
import com.tpi.mslogistica.dto.CambioEstadoTramoRequest;
import com.tpi.mslogistica.dto.TramoDTO;
import com.tpi.mslogistica.service.TramoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tramos")
@Tag(name = "Tramos", description = "Gestión de tramos de las rutas")
public class TramoController {

    private final TramoService tramoService;

    public TramoController(TramoService tramoService) {
        this.tramoService = tramoService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un tramo por ID")
    public ResponseEntity<TramoDTO> obtenerPorId(@PathVariable Long id) {
        TramoDTO dto = tramoService.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/asignar-camion")
    @Operation(summary = "Asignar camión a un tramo")
    public ResponseEntity<TramoDTO> asignarCamion(@PathVariable Long id,
                                                  @RequestBody AsignarCamionTramoRequest request) {
        TramoDTO dto = tramoService.asignarCamion(id, request);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Cambiar el estado de un tramo (INICIAR / FINALIZAR)")
    public ResponseEntity<TramoDTO> cambiarEstado(@PathVariable Long id,
                                                  @RequestBody CambioEstadoTramoRequest request) {
        TramoDTO dto = tramoService.cambiarEstado(id, request);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar un tramo (transportista comienza el recorrido)")
    public ResponseEntity<TramoDTO> iniciarTramo(@PathVariable Long id) {
        TramoDTO dto = tramoService.iniciarTramo(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar un tramo (transportista termina el recorrido)")
    public ResponseEntity<TramoDTO> finalizarTramo(@PathVariable Long id) {
        TramoDTO dto = tramoService.finalizarTramo(id);
        return ResponseEntity.ok(dto);
    }

}
