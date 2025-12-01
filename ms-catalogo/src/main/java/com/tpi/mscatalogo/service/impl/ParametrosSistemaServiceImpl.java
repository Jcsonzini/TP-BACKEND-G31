package com.tpi.mscatalogo.service.impl;

import com.tpi.mscatalogo.domain.ParametrosSistema;
import com.tpi.mscatalogo.dto.ParametrosSistemaDTO;
import com.tpi.mscatalogo.repository.ParametrosSistemaRepository;
import com.tpi.mscatalogo.service.ParametrosSistemaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class ParametrosSistemaServiceImpl implements ParametrosSistemaService {

    private final ParametrosSistemaRepository parametrosSistemaRepository;

    public ParametrosSistemaServiceImpl(ParametrosSistemaRepository parametrosSistemaRepository) {
        this.parametrosSistemaRepository = parametrosSistemaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ParametrosSistemaDTO obtener() {
        ParametrosSistema entity = parametrosSistemaRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No se encontraron parámetros de sistema"));
        return toDTO(entity);
    }

    @Override
    public ParametrosSistemaDTO guardar(ParametrosSistemaDTO dto) {
        ParametrosSistema entity = parametrosSistemaRepository.findAll()
                .stream()
                .findFirst()
                .orElse(new ParametrosSistema());

        entity.setCostoBaseKm(dto.getCostoBaseKm());
        entity.setPrecioLitroCombustible(dto.getPrecioLitroCombustible());
        entity.setConsumoPromedioGeneral(dto.getConsumoPromedioGeneral());

        entity = parametrosSistemaRepository.save(entity);
        return toDTO(entity);
    }

    private ParametrosSistemaDTO toDTO(ParametrosSistema entity) {
        ParametrosSistemaDTO dto = new ParametrosSistemaDTO();
        dto.setId(entity.getId());
        dto.setCostoBaseKm(entity.getCostoBaseKm());
        dto.setPrecioLitroCombustible(entity.getPrecioLitroCombustible());
        dto.setConsumoPromedioGeneral(entity.getConsumoPromedioGeneral());
        return dto;
    }
}
