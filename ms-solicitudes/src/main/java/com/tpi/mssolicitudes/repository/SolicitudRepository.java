package com.tpi.mssolicitudes.repository;

import com.tpi.mssolicitudes.domain.Solicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;  
import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    /**
     * Listar solicitudes de un cliente específico, paginadas.
     */
    Page<Solicitud> findByCliente_Id(Long clienteId, Pageable pageable);

    /**
     * Listar solicitudes asociadas a un contenedor específico, paginadas.
     */
    Page<Solicitud> findByContenedorCodigo(String contenedorCodigo, Pageable pageable);

    @Query("SELECT s FROM Solicitud s WHERE s.estado <> com.tpi.mssolicitudes.domain.EstadoSolicitud.ENTREGADA " +
       "AND s.estado <> com.tpi.mssolicitudes.domain.EstadoSolicitud.CANCELADA")
    List<Solicitud> findPendientesDeEntrega();

    Optional<Solicitud> findTopByContenedorCodigoOrderByIdDesc(String contenedorCodigo);

}
