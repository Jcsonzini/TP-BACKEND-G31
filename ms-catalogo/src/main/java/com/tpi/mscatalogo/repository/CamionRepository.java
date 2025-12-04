package com.tpi.mscatalogo.repository;

import com.tpi.mscatalogo.domain.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CamionRepository extends JpaRepository<Camion, Long> {

    boolean existsByPatente(String patente);

    Optional<Camion> findByPatente(String patente);

    void deleteByPatente(String patente);

    /**
     * Encuentra todos los camiones aptos para transportar un contenedor
     * dado su peso (kg) y volumen (m³).
     * Un camión es apto si su capacidadKg >= pesoContenedor Y su volumenM3 >= volumenContenedor
     */
    @Query("SELECT c FROM Camion c WHERE c.capacidadKg >= :pesoKg AND c.volumenM3 >= :volumenM3")
    List<Camion> findCamionesAptos(@Param("pesoKg") Double pesoKg, @Param("volumenM3") Double volumenM3);
}
