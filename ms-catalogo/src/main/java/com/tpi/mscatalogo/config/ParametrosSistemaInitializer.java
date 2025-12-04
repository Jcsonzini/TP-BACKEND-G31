package com.tpi.mscatalogo.config;

import com.tpi.mscatalogo.domain.ParametrosSistema;
import com.tpi.mscatalogo.repository.ParametrosSistemaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Inicializa los parámetros del sistema al arrancar la aplicación.
 * Estos valores son referencias para cálculos de costo.
 * 
 * NOTA: Los campos costoBaseKm, costoEstadiaDiaria y consumoPromedioGeneral fueron eliminados
 * porque ahora se calculan dinámicamente:
 * - costoBaseKm y consumoPromedio: promedio de camiones aptos para el contenedor
 * - costoEstadiaDiaria: se obtiene de cada depósito específico
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

        // Crear 10 tipos de tarifas diferentes
        List<ParametrosSistema> tarifas = Arrays.asList(
                // 1. Tarifa Estándar Nacional
                ParametrosSistema.builder()
                        .nombre("Tarifa Estándar Nacional")
                        .precioLitroCombustible(1250.0)
                        .costoDescargaCarga(1000.0)
                        .costoTolerancia(50.0)
                        .activa(true)
                        .descripcion("Tarifa estándar para operaciones nacionales 2025")
                        .build(),

                // 2. Tarifa Económica
                ParametrosSistema.builder()
                        .nombre("Tarifa Económica")
                        .precioLitroCombustible(1200.0)
                        .costoDescargaCarga(750.0)
                        .costoTolerancia(30.0)
                        .activa(true)
                        .descripcion("Tarifa reducida para envíos de bajo costo con tiempos flexibles")
                        .build(),

                // 3. Tarifa Premium Express
                ParametrosSistema.builder()
                        .nombre("Tarifa Premium Express")
                        .precioLitroCombustible(1350.0)
                        .costoDescargaCarga(1500.0)
                        .costoTolerancia(100.0)
                        .activa(true)
                        .descripcion("Tarifa premium para entregas urgentes con prioridad máxima")
                        .build(),

                // 4. Tarifa Larga Distancia
                ParametrosSistema.builder()
                        .nombre("Tarifa Larga Distancia")
                        .precioLitroCombustible(1220.0)
                        .costoDescargaCarga(1200.0)
                        .costoTolerancia(40.0)
                        .activa(true)
                        .descripcion("Tarifa optimizada para rutas interprovinciales de más de 500km")
                        .build(),

                // 5. Tarifa Carga Pesada
                ParametrosSistema.builder()
                        .nombre("Tarifa Carga Pesada")
                        .precioLitroCombustible(1300.0)
                        .costoDescargaCarga(2000.0)
                        .costoTolerancia(60.0)
                        .activa(true)
                        .descripcion("Tarifa para transporte de cargas superiores a 10 toneladas")
                        .build(),

                // 6. Tarifa Refrigerado
                ParametrosSistema.builder()
                        .nombre("Tarifa Refrigerado")
                        .precioLitroCombustible(1400.0)
                        .costoDescargaCarga(1800.0)
                        .costoTolerancia(80.0)
                        .activa(true)
                        .descripcion("Tarifa especial para transporte con cadena de frío")
                        .build(),

                // 7. Tarifa Materiales Peligrosos
                ParametrosSistema.builder()
                        .nombre("Tarifa Materiales Peligrosos")
                        .precioLitroCombustible(1350.0)
                        .costoDescargaCarga(2500.0)
                        .costoTolerancia(120.0)
                        .activa(true)
                        .descripcion("Tarifa para transporte de sustancias peligrosas según normativa ADR")
                        .build(),

                // 8. Tarifa Nocturna
                ParametrosSistema.builder()
                        .nombre("Tarifa Nocturna")
                        .precioLitroCombustible(1250.0)
                        .costoDescargaCarga(1300.0)
                        .costoTolerancia(70.0)
                        .activa(true)
                        .descripcion("Tarifa con recargo para operaciones entre 22:00 y 06:00 hs")
                        .build(),

                // 9. Tarifa Zona Rural
                ParametrosSistema.builder()
                        .nombre("Tarifa Zona Rural")
                        .precioLitroCombustible(1280.0)
                        .costoDescargaCarga(1100.0)
                        .costoTolerancia(45.0)
                        .activa(true)
                        .descripcion("Tarifa para entregas en zonas rurales o de difícil acceso")
                        .build(),

                // 10. Tarifa Corporativa
                ParametrosSistema.builder()
                        .nombre("Tarifa Corporativa")
                        .precioLitroCombustible(1200.0)
                        .costoDescargaCarga(900.0)
                        .costoTolerancia(35.0)
                        .activa(true)
                        .descripcion("Tarifa con descuento para clientes corporativos con contrato anual")
                        .build()
        );

        parametrosSistemaRepository.saveAll(tarifas);
    }
}
