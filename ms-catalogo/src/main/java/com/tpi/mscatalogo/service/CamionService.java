package com.tpi.mscatalogo.service;

import com.tpi.mscatalogo.dto.CamionDTO;
import com.tpi.mscatalogo.dto.PromediosCamionesAptosDTO;
import java.util.List;

public interface CamionService {

    CamionDTO crear(CamionDTO dto);

    CamionDTO actualizar(String patente, CamionDTO dto);

    void eliminar(String patente);

    CamionDTO obtenerPorId(Long id);

    CamionDTO obtenerPorPatente(String patente);

    List<CamionDTO> listar();

    /**
     * Calcula los promedios de consumo (litros/km) y costo base ($/km) de los camiones
     * que pueden transportar un contenedor con el peso y volumen especificados.
     * 
     * @param pesoKg peso del contenedor en kg
     * @param volumenM3 volumen del contenedor en m³
     * @return DTO con los promedios calculados
     */
    PromediosCamionesAptosDTO obtenerPromediosCamionesAptos(Double pesoKg, Double volumenM3);
}
