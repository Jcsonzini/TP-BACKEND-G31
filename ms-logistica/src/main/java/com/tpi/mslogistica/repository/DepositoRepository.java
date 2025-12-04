package com.tpi.mslogistica.repository;

import com.tpi.mslogistica.domain.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepositoRepository extends JpaRepository<Deposito, Long> {
    
    /**
     * Busca un depósito por nombre (para obtener el costo de estadía específico)
     */
    Optional<Deposito> findByNombreContainingIgnoreCase(String nombre);
}
