package com.tpi.mscatalogo.web;

import com.tpi.mscatalogo.dto.ParametrosSistemaDTO;
import com.tpi.mscatalogo.service.ParametrosSistemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parametros-sistema")
@Tag(name = "Parámetros del sistema", description = "Configuración global de costos del sistema")
public class ParametrosSistemaController {

    private final ParametrosSistemaService parametrosSistemaService;

    public ParametrosSistemaController(ParametrosSistemaService parametrosSistemaService) {
        this.parametrosSistemaService = parametrosSistemaService;
    }

    @GetMapping
    @Operation(summary = "Obtener los parámetros actuales del sistema")
    public ResponseEntity<ParametrosSistemaDTO> obtener() {
        ParametrosSistemaDTO dto = parametrosSistemaService.obtener();
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    @Operation(summary = "Crear o actualizar los parámetros del sistema")
    public ResponseEntity<ParametrosSistemaDTO> guardar(@RequestBody ParametrosSistemaDTO dto) {
        ParametrosSistemaDTO guardado = parametrosSistemaService.guardar(dto);
        return ResponseEntity.status(HttpStatus.OK).body(guardado);
    }
}
