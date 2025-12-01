package com.tpi.mscatalogo.repository;

import com.tpi.mscatalogo.domain.Camion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CamionRepository extends JpaRepository<Camion, Long> {

    boolean existsByPatente(String patente);

    Optional<Camion> findByPatente(String patente);

    void deleteByPatente(String patente);
}
