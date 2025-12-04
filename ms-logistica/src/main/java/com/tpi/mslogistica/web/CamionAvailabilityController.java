package com.tpi.mslogistica.web;

import com.tpi.mslogistica.client.dto.CamionRemotoDTO;
import com.tpi.mslogistica.service.CamionAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camiones")
@RequiredArgsConstructor
public class CamionAvailabilityController {

    private final CamionAvailabilityService service;

    @GetMapping("/libres")
    public ResponseEntity<List<CamionRemotoDTO>> listarLibres() {
        return ResponseEntity.ok(service.obtenerCamionesLibres());
    }

    @GetMapping("/ocupados")
    public ResponseEntity<List<CamionRemotoDTO>> listarOcupados() {
        return ResponseEntity.ok(service.obtenerCamionesOcupados());
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<String> estado(@PathVariable Long id) {

        boolean ocupado = service.obtenerCamionesOcupados().stream()
                .anyMatch(c -> c.getId().equals(id));

        return ResponseEntity.ok(ocupado ? "OCUPADO" : "LIBRE");
    }
}
