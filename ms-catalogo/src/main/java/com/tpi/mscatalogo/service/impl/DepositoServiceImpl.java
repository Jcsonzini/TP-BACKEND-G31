package com.tpi.mscatalogo.service.impl;

import com.tpi.mscatalogo.domain.Deposito;
import com.tpi.mscatalogo.dto.DepositoDTO;
import com.tpi.mscatalogo.repository.DepositoRepository;
import com.tpi.mscatalogo.service.DepositoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class DepositoServiceImpl implements DepositoService {

    private final DepositoRepository depositoRepository;

    public DepositoServiceImpl(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    @Override
    public DepositoDTO crear(DepositoDTO dto) {
        Deposito entity = toEntity(dto);
        entity.setId(null); // que lo genere la DB
        entity = depositoRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    public DepositoDTO actualizar(Long id, DepositoDTO dto) {
        Deposito entity = depositoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Depósito no encontrado: " + id));

        entity.setNombre(dto.getNombre());
        entity.setDireccion(dto.getDireccion());
        entity.setLocalidad(dto.getLocalidad());
        entity.setProvincia(dto.getProvincia());
        entity.setLatitud(dto.getLatitud());
        entity.setLongitud(dto.getLongitud());
        entity.setCapacidadMaxima(dto.getCapacidadMaxima());
        entity.setCostoEstadiaDiaria(dto.getCostoEstadiaDiaria());

        entity = depositoRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    public void eliminar(Long id) {
        if (!depositoRepository.existsById(id)) {
            throw new NoSuchElementException("Depósito no encontrado: " + id);
        }
        depositoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DepositoDTO obtenerPorId(Long id) {
        return depositoRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NoSuchElementException("Depósito no encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepositoDTO> listar(Pageable pageable) {
        return depositoRepository.findAll(pageable)
                .map(this::toDTO);
    }

    // ---------- Mappers ----------

    private DepositoDTO toDTO(Deposito entity) {
        DepositoDTO dto = new DepositoDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDireccion(entity.getDireccion());
        dto.setLocalidad(entity.getLocalidad());
        dto.setProvincia(entity.getProvincia());
        dto.setLatitud(entity.getLatitud());
        dto.setLongitud(entity.getLongitud());
        dto.setCapacidadMaxima(entity.getCapacidadMaxima());
        dto.setCostoEstadiaDiaria(entity.getCostoEstadiaDiaria());
        return dto;
    }

    private Deposito toEntity(DepositoDTO dto) {
        Deposito entity = new Deposito();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setDireccion(dto.getDireccion());
        entity.setLocalidad(dto.getLocalidad());
        entity.setProvincia(dto.getProvincia());
        entity.setLatitud(dto.getLatitud());
        entity.setLongitud(dto.getLongitud());
        entity.setCapacidadMaxima(dto.getCapacidadMaxima());
        entity.setCostoEstadiaDiaria(dto.getCostoEstadiaDiaria());
        return entity;
    }
}
