package com.tpi.mscatalogo.repository;

import com.tpi.mscatalogo.domain.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {

    boolean existsByCodigo(String codigo);

    Optional<Contenedor> findByCodigo(String codigo);

    void deleteByCodigo(String codigo);
}
