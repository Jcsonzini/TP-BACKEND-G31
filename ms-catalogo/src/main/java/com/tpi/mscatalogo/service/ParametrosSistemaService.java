package com.tpi.mscatalogo.service;

import com.tpi.mscatalogo.dto.ParametrosSistemaDTO;

public interface ParametrosSistemaService {

    ParametrosSistemaDTO obtener();          // trae el registro actual (o lanza excepción)

    ParametrosSistemaDTO guardar(ParametrosSistemaDTO dto);  // crea/actualiza
}
