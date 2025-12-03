package com.tpi.mscatalogo.web;

import com.tpi.mscatalogo.dto.ParametrosSistemaDTO;
import com.tpi.mscatalogo.service.ParametrosSistemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parametros-sistema")
@Tag(name = "Parámetros del sistema (Tarifas)", description = "CRUD de tarifas y configuración global de costos del sistema")
public class ParametrosSistemaController {

    private final ParametrosSistemaService parametrosSistemaService;

    public ParametrosSistemaController(ParametrosSistemaService parametrosSistemaService) {
        this.parametrosSistemaService = parametrosSistemaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las tarifas")
    public ResponseEntity<List<ParametrosSistemaDTO>> listar() {
        List<ParametrosSistemaDTO> dtos = parametrosSistemaService.listar();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/activas")
    @Operation(summary = "Listar solo tarifas activas")
    public ResponseEntity<List<ParametrosSistemaDTO>> listarActivas() {
        List<ParametrosSistemaDTO> dtos = parametrosSistemaService.listarActivas();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tarifa por ID")
    public ResponseEntity<ParametrosSistemaDTO> obtenerPorId(@PathVariable Long id) {
        ParametrosSistemaDTO dto = parametrosSistemaService.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Obtener tarifa por nombre")
    public ResponseEntity<ParametrosSistemaDTO> obtenerPorNombre(@PathVariable String nombre) {
        ParametrosSistemaDTO dto = parametrosSistemaService.obtenerPorNombre(nombre);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Crear nueva tarifa")
    public ResponseEntity<ParametrosSistemaDTO> crear(@RequestBody ParametrosSistemaDTO dto) {
        ParametrosSistemaDTO guardado = parametrosSistemaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tarifa existente")
    public ResponseEntity<ParametrosSistemaDTO> actualizar(@PathVariable Long id, @RequestBody ParametrosSistemaDTO dto) {
        ParametrosSistemaDTO guardado = parametrosSistemaService.actualizar(id, dto);
        return ResponseEntity.ok(guardado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tarifa")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        parametrosSistemaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint legacy para mantener compatibilidad
    @GetMapping("/actual")
    @Operation(summary = "Obtener los parámetros actuales del sistema (primer registro)")
    public ResponseEntity<ParametrosSistemaDTO> obtener() {
        ParametrosSistemaDTO dto = parametrosSistemaService.obtener();
        return ResponseEntity.ok(dto);
    }

    // Endpoint legacy para mantener compatibilidad
    @PutMapping
    @Operation(summary = "Crear o actualizar los parámetros del sistema (legacy)")
    public ResponseEntity<ParametrosSistemaDTO> guardar(@RequestBody ParametrosSistemaDTO dto) {
        ParametrosSistemaDTO guardado = parametrosSistemaService.guardar(dto);
        return ResponseEntity.status(HttpStatus.OK).body(guardado);
    }
}
