package com.tpi.mscatalogo.web;

import com.tpi.mscatalogo.dto.ContenedorDTO;
import com.tpi.mscatalogo.service.ContenedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenedores")
@Tag(name = "Contenedores", description = "Gestión de contenedores del catálogo")
public class ContenedorController {

    private final ContenedorService contenedorService;

    public ContenedorController(ContenedorService contenedorService) {
        this.contenedorService = contenedorService;
    }

    @PostMapping
    @Operation(summary = "Crear un contenedor")
    public ResponseEntity<ContenedorDTO> crear(@RequestBody ContenedorDTO dto) {
        ContenedorDTO creado = contenedorService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{codigo}")
    @Operation(summary = "Actualizar un contenedor por código")
    public ResponseEntity<ContenedorDTO> actualizar(@PathVariable String codigo,
                                                    @RequestBody ContenedorDTO dto) {
        ContenedorDTO actualizado = contenedorService.actualizar(codigo, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Eliminar un contenedor por código")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
        contenedorService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Obtener un contenedor por código")
    public ResponseEntity<ContenedorDTO> obtenerPorCodigo(@PathVariable String codigo) {
        ContenedorDTO dto = contenedorService.obtenerPorCodigo(codigo);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Listar todos los contenedores")
    public ResponseEntity<List<ContenedorDTO>> listar() {
        List<ContenedorDTO> lista = contenedorService.listar();
        return ResponseEntity.ok(lista);
    }
}
