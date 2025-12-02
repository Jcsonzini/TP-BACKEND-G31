package com.tpi.mslogistica.web;

import com.tpi.mslogistica.dto.RutaCreateRequest;
import com.tpi.mslogistica.dto.RutaDTO;
import com.tpi.mslogistica.service.RutaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas")
@Tag(name = "Rutas", description = "Gestión de rutas logísticas")
public class RutaController {

    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    @PostMapping
    @Operation(summary = "Crear una ruta logística (estimación inicial)")
    public ResponseEntity<RutaDTO> crear(@RequestBody RutaCreateRequest request) {
        RutaDTO creada = rutaService.crearRuta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PostMapping("/generar-tentativas")
    @Operation(summary = "Generar rutas tentativas para una solicitud")
    public ResponseEntity<List<RutaDTO>> generarTentativas(@RequestBody RutaCreateRequest request) {

        // Por ahora siempre generamos 3 rutas tentativas
        List<RutaDTO> rutas = rutaService.generarRutasTentativas(request, 3);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rutas);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener una ruta por ID")
    public ResponseEntity<RutaDTO> obtenerPorId(@PathVariable Long id) {
        RutaDTO dto = rutaService.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Listar todas las rutas")
    public ResponseEntity<List<RutaDTO>> listar() {
        List<RutaDTO> lista = rutaService.listar();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/por-solicitud/{solicitudId}")
    @Operation(summary = "Listar rutas asociadas a una solicitud")
    public ResponseEntity<List<RutaDTO>> listarPorSolicitud(@PathVariable Long solicitudId) {
        List<RutaDTO> lista = rutaService.listarPorSolicitud(solicitudId);
        return ResponseEntity.ok(lista);
    }
}
