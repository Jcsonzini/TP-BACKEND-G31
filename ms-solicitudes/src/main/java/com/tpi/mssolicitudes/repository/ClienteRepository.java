package com.tpi.mssolicitudes.repository;

import com.tpi.mssolicitudes.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Buscar un cliente por email (usado para evitar duplicados o para búsquedas rápidas).
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Buscar un cliente por identificación (DNI/CUIT, etc.).
     * Podés usarlo para lógica de "obtener o crear".
     */
    Optional<Cliente> findByIdentificacion(String identificacion);
}
