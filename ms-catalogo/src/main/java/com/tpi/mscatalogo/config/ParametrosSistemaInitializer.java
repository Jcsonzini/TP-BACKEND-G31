package com.tpi.mscatalogo.config;

import com.tpi.mscatalogo.domain.ParametrosSistema;
import com.tpi.mscatalogo.repository.ParametrosSistemaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Inicializa los parámetros del sistema al arrancar la aplicación.
 * Estos valores son referencias para cálculos de costo.
 */
@Component
@RequiredArgsConstructor
public class ParametrosSistemaInitializer {
    private final ParametrosSistemaRepository parametrosSistemaRepository;

    @PostConstruct
    public void init() {
        // Solo inicializar si no existen parámetros
        if (parametrosSistemaRepository.count() > 0) {
            return;
        }

        // Crear parámetros del sistema con valores iniciales
        ParametrosSistema params = ParametrosSistema.builder()
                .nombre("Tarifa Standard 2025")
                // Costo base promedio por km (referencia del sistema)
                .costoBaseKm(150.0)
                // Precio del litro de combustible actualizado (entre 1000 y 1500)
                .precioLitroCombustible(1250.0)  // ARS por litro
                // Consumo promedio general: (0.32+0.30+0.315+0.305+0.33+0.31+0.335+0.325+0.298+0.29+0.34) / 11
                // = 3.448 / 11 = 0.313636... ≈ 0.314
                .consumoPromedioGeneral(0.314)   // litros/km promedio
                .costoEstadiaDiaria(500.0)       // ARS por día
                .costoDescargaCarga(1000.0)      // ARS por operación
                .costoTolerancia(50.0)           // ARS por hora de retraso
                .activa(true)
                .descripcion("Tarifa estándar para operaciones nacionales 2025")
                .build();

        parametrosSistemaRepository.save(params);
    }
}
