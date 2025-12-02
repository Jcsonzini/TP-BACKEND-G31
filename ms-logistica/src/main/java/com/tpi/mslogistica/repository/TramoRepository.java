package com.tpi.mslogistica.repository;

import com.tpi.mslogistica.domain.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TramoRepository extends JpaRepository<Tramo, Long> {

    /**
     * Obtener todos los tramos de una ruta, ordenados por el campo 'orden'.
     */
    List<Tramo> findByRuta_IdOrderByOrdenAsc(Long rutaId);
}
