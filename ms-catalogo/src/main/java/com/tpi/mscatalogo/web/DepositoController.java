package com.tpi.mscatalogo.web;

import com.tpi.mscatalogo.dto.DepositoDTO;
import com.tpi.mscatalogo.service.DepositoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depositos")
@Tag(name = "Depósitos", description = "Gestión de depósitos del catálogo")
public class DepositoController {

    private final DepositoService depositoService;

    public DepositoController(DepositoService depositoService) {
        this.depositoService = depositoService;
    }

    @PostMapping
    @Operation(summary = "Crear un depósito")
    public ResponseEntity<DepositoDTO> crear(@RequestBody DepositoDTO dto) {
        DepositoDTO creado = depositoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un depósito por id")
    public ResponseEntity<DepositoDTO> actualizar(@PathVariable Long id,
                                                  @RequestBody DepositoDTO dto) {
        DepositoDTO actualizado = depositoService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un depósito por id")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        depositoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un depósito por id")
    public ResponseEntity<DepositoDTO> obtenerPorId(@PathVariable Long id) {
        DepositoDTO dto = depositoService.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Listar todos los depósitos")
    public ResponseEntity<List<DepositoDTO>> listar() {
        List<DepositoDTO> lista = depositoService.listar();
        return ResponseEntity.ok(lista);
    }
}
