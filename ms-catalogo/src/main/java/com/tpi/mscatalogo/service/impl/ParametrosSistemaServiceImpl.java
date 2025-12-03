package com.tpi.mscatalogo.service.impl;

import com.tpi.mscatalogo.domain.ParametrosSistema;
import com.tpi.mscatalogo.dto.ParametrosSistemaDTO;
import com.tpi.mscatalogo.repository.ParametrosSistemaRepository;
import com.tpi.mscatalogo.service.ParametrosSistemaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
        entity.setCostoEstadiaDiaria(dto.getCostoEstadiaDiaria());
        entity.setCostoDescargaCarga(dto.getCostoDescargaCarga());
        entity.setCostoTolerancia(dto.getCostoTolerancia());
        entity.setNombre(dto.getNombre() != null ? dto.getNombre() : entity.getNombre());
        entity.setActiva(dto.getActiva() != null ? dto.getActiva() : true);
        entity.setDescripcion(dto.getDescripcion());

        entity = parametrosSistemaRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParametrosSistemaDTO> listar() {
        return parametrosSistemaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ParametrosSistemaDTO obtenerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        ParametrosSistema entity = parametrosSistemaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tarifa no encontrada con id: " + id));
        return toDTO(entity);
    }

    @Override
    public ParametrosSistemaDTO crear(ParametrosSistemaDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la tarifa no puede ser vacío");
        }
        
        ParametrosSistema entity = new ParametrosSistema();
        entity.setNombre(dto.getNombre());
        entity.setCostoBaseKm(dto.getCostoBaseKm() != null ? dto.getCostoBaseKm() : 150.0);
        entity.setCostoEstadiaDiaria(dto.getCostoEstadiaDiaria() != null ? dto.getCostoEstadiaDiaria() : 500.0);
        entity.setCostoDescargaCarga(dto.getCostoDescargaCarga() != null ? dto.getCostoDescargaCarga() : 1000.0);
        entity.setCostoTolerancia(dto.getCostoTolerancia() != null ? dto.getCostoTolerancia() : 50.0);
        entity.setPrecioLitroCombustible(dto.getPrecioLitroCombustible() != null ? dto.getPrecioLitroCombustible() : 250.0);
        entity.setConsumoPromedioGeneral(dto.getConsumoPromedioGeneral() != null ? dto.getConsumoPromedioGeneral() : 0.08);
        entity.setActiva(dto.getActiva() != null ? dto.getActiva() : true);
        entity.setDescripcion(dto.getDescripcion());

        entity = parametrosSistemaRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    public ParametrosSistemaDTO actualizar(Long id, ParametrosSistemaDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        
        ParametrosSistema entity = parametrosSistemaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tarifa no encontrada con id: " + id));

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            entity.setNombre(dto.getNombre());
        }
        if (dto.getCostoBaseKm() != null) {
            entity.setCostoBaseKm(dto.getCostoBaseKm());
        }
        if (dto.getCostoEstadiaDiaria() != null) {
            entity.setCostoEstadiaDiaria(dto.getCostoEstadiaDiaria());
        }
        if (dto.getCostoDescargaCarga() != null) {
            entity.setCostoDescargaCarga(dto.getCostoDescargaCarga());
        }
        if (dto.getCostoTolerancia() != null) {
            entity.setCostoTolerancia(dto.getCostoTolerancia());
        }
        if (dto.getPrecioLitroCombustible() != null) {
            entity.setPrecioLitroCombustible(dto.getPrecioLitroCombustible());
        }
        if (dto.getConsumoPromedioGeneral() != null) {
            entity.setConsumoPromedioGeneral(dto.getConsumoPromedioGeneral());
        }
        if (dto.getActiva() != null) {
            entity.setActiva(dto.getActiva());
        }
        if (dto.getDescripcion() != null) {
            entity.setDescripcion(dto.getDescripcion());
        }

        entity = parametrosSistemaRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        
        if (!parametrosSistemaRepository.existsById(id)) {
            throw new NoSuchElementException("Tarifa no encontrada con id: " + id);
        }
        
        parametrosSistemaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ParametrosSistemaDTO obtenerPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío");
        }
        
        ParametrosSistema entity = parametrosSistemaRepository.findAll()
                .stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Tarifa no encontrada con nombre: " + nombre));
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParametrosSistemaDTO> listarActivas() {
        return parametrosSistemaRepository.findAll()
                .stream()
                .filter(p -> p.getActiva() != null && p.getActiva())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ParametrosSistemaDTO toDTO(ParametrosSistema entity) {
        ParametrosSistemaDTO dto = new ParametrosSistemaDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setCostoBaseKm(entity.getCostoBaseKm());
        dto.setCostoEstadiaDiaria(entity.getCostoEstadiaDiaria());
        dto.setCostoDescargaCarga(entity.getCostoDescargaCarga());
        dto.setCostoTolerancia(entity.getCostoTolerancia());
        dto.setPrecioLitroCombustible(entity.getPrecioLitroCombustible());
        dto.setConsumoPromedioGeneral(entity.getConsumoPromedioGeneral());
        dto.setActiva(entity.getActiva());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }
}
