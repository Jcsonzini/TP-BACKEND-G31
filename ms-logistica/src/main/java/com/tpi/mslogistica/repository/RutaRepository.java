package com.tpi.mslogistica.repository;

import com.tpi.mslogistica.domain.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RutaRepository extends JpaRepository<Ruta, Long> {

    /**
     * Buscar todas las rutas asociadas a una solicitud de ms-solicitudes.
     */
    List<Ruta> findBySolicitudId(Long solicitudId);
}
