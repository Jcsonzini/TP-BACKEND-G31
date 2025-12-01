package com.tpi.mscatalogo.service.impl;

import com.tpi.mscatalogo.domain.Camion;
import com.tpi.mscatalogo.dto.CamionDTO;
import com.tpi.mscatalogo.repository.CamionRepository;
import com.tpi.mscatalogo.service.CamionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class CamionServiceImpl implements CamionService {

    private final CamionRepository camionRepository;

    public CamionServiceImpl(CamionRepository camionRepository) {
        this.camionRepository = camionRepository;
    }

    @Override
    public CamionDTO crear(CamionDTO dto) {
        // Validar unicidad por patente
        if (camionRepository.existsByPatente(dto.getPatente())) {
            throw new IllegalArgumentException("Ya existe un camión con patente: " + dto.getPatente());
        }

        Camion entity = toEntity(dto);
        entity.setId(null); // que lo genere la DB
        entity = camionRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    public CamionDTO actualizar(String patente, CamionDTO dto) {
        Camion entity = camionRepository.findByPatente(patente)
                .orElseThrow(() -> new NoSuchElementException("Camión no encontrado: " + patente));

        // Opcional: si querés permitir cambiar la patente, lo podés hacer acá
        entity.setModelo(dto.getModelo());
        entity.setCapacidadKg(dto.getCapacidadKg());
        entity.setVolumenM3(dto.getVolumenM3());
        entity.setConsumoLitrosKm(dto.getConsumoLitrosKm());
        entity.setCostoBaseKm(dto.getCostoBaseKm());
        entity.setEmpresaTransportista(dto.getEmpresaTransportista());

        entity = camionRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    public void eliminar(String patente) {
        if (!camionRepository.existsByPatente(patente)) {
            throw new NoSuchElementException("Camión no encontrado: " + patente);
        }
        camionRepository.deleteByPatente(patente);
    }

    @Override
    @Transactional(readOnly = true)
    public CamionDTO obtenerPorPatente(String patente) {
        return camionRepository.findByPatente(patente)
                .map(this::toDTO)
                .orElseThrow(() -> new NoSuchElementException("Camión no encontrado: " + patente));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CamionDTO> listar(Pageable pageable) {
        return camionRepository.findAll(pageable)
                .map(this::toDTO);
    }

    // ---------- Mappers ----------

    private CamionDTO toDTO(Camion entity) {
        CamionDTO dto = new CamionDTO();
        dto.setId(entity.getId());
        dto.setPatente(entity.getPatente());
        dto.setModelo(entity.getModelo());
        dto.setCapacidadKg(entity.getCapacidadKg());
        dto.setVolumenM3(entity.getVolumenM3());
        dto.setConsumoLitrosKm(entity.getConsumoLitrosKm());
        dto.setCostoBaseKm(entity.getCostoBaseKm());
        dto.setEmpresaTransportista(entity.getEmpresaTransportista());
        return dto;
    }

    private Camion toEntity(CamionDTO dto) {
        Camion entity = new Camion();
        entity.setId(dto.getId());
        entity.setPatente(dto.getPatente());
        entity.setModelo(dto.getModelo());
        entity.setCapacidadKg(dto.getCapacidadKg());
        entity.setVolumenM3(dto.getVolumenM3());
        entity.setConsumoLitrosKm(dto.getConsumoLitrosKm());
        entity.setCostoBaseKm(dto.getCostoBaseKm());
        entity.setEmpresaTransportista(dto.getEmpresaTransportista());
        return entity;
    }
}
