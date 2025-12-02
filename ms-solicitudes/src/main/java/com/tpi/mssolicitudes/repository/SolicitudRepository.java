package com.tpi.mssolicitudes.repository;

import com.tpi.mssolicitudes.domain.Solicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    /**
     * Buscar una solicitud por su número humano (ej: SOL-0001).
     */
    Optional<Solicitud> findByNumeroSolicitud(String numeroSolicitud);

    /**
     * Listar solicitudes de un cliente específico, paginadas.
     */
    Page<Solicitud> findByCliente_Id(Long clienteId, Pageable pageable);

    /**
     * Listar solicitudes asociadas a un contenedor específico, paginadas.
     */
    Page<Solicitud> findByContenedorCodigo(String contenedorCodigo, Pageable pageable);
}
