package com.tpi.mscatalogo.service.impl;

import com.tpi.mscatalogo.domain.Contenedor;
import com.tpi.mscatalogo.domain.Deposito;
import com.tpi.mscatalogo.domain.EstadoContenedor;
import com.tpi.mscatalogo.domain.TipoContenedor;
import com.tpi.mscatalogo.dto.ContenedorDTO;
import com.tpi.mscatalogo.repository.ContenedorRepository;
import com.tpi.mscatalogo.repository.DepositoRepository;
import com.tpi.mscatalogo.service.ContenedorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class ContenedorServiceImpl implements ContenedorService {

    private final ContenedorRepository contenedorRepository;
    private final DepositoRepository depositoRepository;

    public ContenedorServiceImpl(ContenedorRepository contenedorRepository,
                                 DepositoRepository depositoRepository) {
        this.contenedorRepository = contenedorRepository;
        this.depositoRepository = depositoRepository;
    }

    @Override
    public ContenedorDTO crear(ContenedorDTO dto) {
        if (contenedorRepository.existsByCodigo(dto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un contenedor con código: " + dto.getCodigo());
        }

        Contenedor entity = toEntity(dto);
        entity.setId(null); // que la DB genere el id
        entity = contenedorRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    public ContenedorDTO actualizar(String codigo, ContenedorDTO dto) {
        if (codigo == null) {
            throw new IllegalArgumentException("El código de contenedor no puede ser nulo");
        }
        Contenedor entity = contenedorRepository.findByCodigo(codigo)
                .orElseThrow(() -> new NoSuchElementException("Contenedor no encontrado: " + codigo));

        // Si quisieras permitir cambiar el código, podrías hacerlo acá.
        if (dto.getTipo() != null) {
            entity.setTipo(TipoContenedor.valueOf(dto.getTipo()));
        }

        entity.setCapacidadKg(dto.getCapacidadKg());
        entity.setPesoReal(dto.getPesoReal());
        entity.setVolumenReal(dto.getVolumenReal());

        if (dto.getEstado() != null) {
            entity.setEstado(EstadoContenedor.valueOf(dto.getEstado()));
        }

        Long depositoId = dto.getDepositoActualId();
        if (depositoId != null) {
            Long nonNullId = Objects.requireNonNull(depositoId);
            Deposito deposito = depositoRepository.findById(nonNullId)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Depósito no encontrado: " + nonNullId));
            entity.setDepositoActual(deposito);
        } else {
            entity.setDepositoActual(null);
        }

        entity = contenedorRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    public void eliminar(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("El código de contenedor no puede ser nulo");
        }
        if (!contenedorRepository.existsByCodigo(codigo)) {
            throw new NoSuchElementException("Contenedor no encontrado: " + codigo);
        }
        contenedorRepository.deleteByCodigo(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public ContenedorDTO obtenerPorCodigo(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("El código de contenedor no puede ser nulo");
        }
        return contenedorRepository.findByCodigo(codigo)
                .map(this::toDTO)
                .orElseThrow(() -> new NoSuchElementException("Contenedor no encontrado: " + codigo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContenedorDTO> listar() {
        return contenedorRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ---------- Mappers ----------

    private ContenedorDTO toDTO(Contenedor entity) {
        ContenedorDTO dto = new ContenedorDTO();
        dto.setId(entity.getId());
        dto.setCodigo(entity.getCodigo());
        dto.setTipo(entity.getTipo() != null ? entity.getTipo().name() : null);
        dto.setCapacidadKg(entity.getCapacidadKg());
        dto.setPesoReal(entity.getPesoReal());
        dto.setVolumenReal(entity.getVolumenReal());
        dto.setEstado(entity.getEstado() != null ? entity.getEstado().name() : null);
        dto.setDepositoActualId(
                entity.getDepositoActual() != null ? entity.getDepositoActual().getId() : null
        );
        return dto;
    }

    private Contenedor toEntity(ContenedorDTO dto) {
        Contenedor entity = new Contenedor();
        entity.setId(dto.getId());
        entity.setCodigo(dto.getCodigo());

        if (dto.getTipo() != null) {
            entity.setTipo(TipoContenedor.valueOf(dto.getTipo()));
        }

        entity.setCapacidadKg(dto.getCapacidadKg());
        entity.setPesoReal(dto.getPesoReal());
        entity.setVolumenReal(dto.getVolumenReal());

        if (dto.getEstado() != null) {
            entity.setEstado(EstadoContenedor.valueOf(dto.getEstado()));
        }

        Long depositoId = dto.getDepositoActualId();
        if (depositoId != null) {
            Long nonNullId = Objects.requireNonNull(depositoId);
            Deposito deposito = depositoRepository.findById(nonNullId)
                .orElseThrow(() -> new NoSuchElementException(
                    "Deposito no encontrado: " + nonNullId));
            entity.setDepositoActual(deposito);
        }

        return entity;
    }
}
