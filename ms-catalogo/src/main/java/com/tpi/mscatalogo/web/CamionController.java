package com.tpi.mscatalogo.web;

import com.tpi.mscatalogo.dto.CamionDTO;
import com.tpi.mscatalogo.service.CamionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/camiones")
@Tag(name = "Camiones", description = "Gestión de camiones del catálogo")
public class CamionController {

    private final CamionService camionService;

    public CamionController(CamionService camionService) {
        this.camionService = camionService;
    }

    @PostMapping
    @Operation(summary = "Crear un camión")
    public ResponseEntity<CamionDTO> crear(@RequestBody CamionDTO dto) {
        CamionDTO creado = camionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{patente}")
    @Operation(summary = "Actualizar un camión por patente")
    public ResponseEntity<CamionDTO> actualizar(@PathVariable String patente,
                                                @RequestBody CamionDTO dto) {
        CamionDTO actualizado = camionService.actualizar(patente, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{patente}")
    @Operation(summary = "Eliminar un camión por patente")
    public ResponseEntity<Void> eliminar(@PathVariable String patente) {
        camionService.eliminar(patente);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{patente}")
    @Operation(summary = "Obtener un camión por patente")
    public ResponseEntity<CamionDTO> obtenerPorPatente(@PathVariable String patente) {
        CamionDTO dto = camionService.obtenerPorPatente(patente);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Listar camiones con paginación")
    public ResponseEntity<Page<CamionDTO>> listar(Pageable pageable) {
        Page<CamionDTO> page = camionService.listar(pageable);
        return ResponseEntity.ok(page);
    }
}
