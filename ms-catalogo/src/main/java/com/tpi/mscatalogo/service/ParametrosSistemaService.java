package com.tpi.mscatalogo.service;

import com.tpi.mscatalogo.dto.ParametrosSistemaDTO;
import java.util.List;

public interface ParametrosSistemaService {

    ParametrosSistemaDTO obtener();          // trae el registro actual (o lanza excepción)

    ParametrosSistemaDTO guardar(ParametrosSistemaDTO dto);  // crea/actualiza

    // CRUD Completo
    List<ParametrosSistemaDTO> listar();     // listar todas las tarifas
    
    ParametrosSistemaDTO obtenerPorId(Long id);  // obtener por ID
    
    ParametrosSistemaDTO crear(ParametrosSistemaDTO dto);  // crear nueva tarifa
    
    ParametrosSistemaDTO actualizar(Long id, ParametrosSistemaDTO dto);  // actualizar tarifa existente
    
    void eliminar(Long id);  // eliminar tarifa
    
    ParametrosSistemaDTO obtenerPorNombre(String nombre);  // obtener por nombre
    
    List<ParametrosSistemaDTO> listarActivas();  // listar solo tarifas activas
}
