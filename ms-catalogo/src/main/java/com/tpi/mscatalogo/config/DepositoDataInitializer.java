package com.tpi.mscatalogo.config;

import com.tpi.mscatalogo.domain.Deposito;
import com.tpi.mscatalogo.repository.DepositoRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DepositoDataInitializer {

    private final DepositoRepository depositoRepository;

    @PostConstruct
    public void init() {
        // Si ya hay depósitos cargados, no hacemos nada
        if (depositoRepository.count() > 0) {
            return;
        }

        List<Deposito> depositos = List.of(
                Deposito.builder()
                        .nombre("Depósito Salta Norte")
                        .direccion("Parque Industrial Salta")
                        .localidad("Salta")
                        .provincia("Salta")
                        .latitud(new BigDecimal("-24.785900"))
                        .longitud(new BigDecimal("-65.411700"))
                        .capacidadMaxima(300)
                        .costoEstadiaDiaria(18000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Tucumán Sur")
                        .direccion("Av. Circunvalación 5000")
                        .localidad("San Miguel de Tucumán")
                        .provincia("Tucumán")
                        .latitud(new BigDecimal("-26.808300"))
                        .longitud(new BigDecimal("-65.217600"))
                        .capacidadMaxima(300)
                        .costoEstadiaDiaria(18000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Jujuy Andino")
                        .direccion("Ruta 9 Km 10")
                        .localidad("San Salvador de Jujuy")
                        .provincia("Jujuy")
                        .latitud(new BigDecimal("-24.185800"))
                        .longitud(new BigDecimal("-65.299500"))
                        .capacidadMaxima(300)
                        .costoEstadiaDiaria(18000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Resistencia Chaco")
                        .direccion("Parque Industrial Resistencia")
                        .localidad("Resistencia")
                        .provincia("Chaco")
                        .latitud(new BigDecimal("-27.451000"))
                        .longitud(new BigDecimal("-58.986700"))
                        .capacidadMaxima(300)
                        .costoEstadiaDiaria(18000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Corrientes Río")
                        .direccion("Costanera Sur 2000")
                        .localidad("Corrientes")
                        .provincia("Corrientes")
                        .latitud(new BigDecimal("-27.467800"))
                        .longitud(new BigDecimal("-58.834400"))
                        .capacidadMaxima(300)
                        .costoEstadiaDiaria(18000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Posadas Misionero")
                        .direccion("Acceso Sur Km 5")
                        .localidad("Posadas")
                        .provincia("Misiones")
                        .latitud(new BigDecimal("-27.362100"))
                        .longitud(new BigDecimal("-55.900900"))
                        .capacidadMaxima(300)
                        .costoEstadiaDiaria(18000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Mendoza Oeste")
                        .direccion("Acceso Este Km 12")
                        .localidad("Mendoza")
                        .provincia("Mendoza")
                        .latitud(new BigDecimal("-32.889500"))
                        .longitud(new BigDecimal("-68.845800"))
                        .capacidadMaxima(350)
                        .costoEstadiaDiaria(19000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito San Juan Centro")
                        .direccion("Ruta 40 Km 20")
                        .localidad("San Juan")
                        .provincia("San Juan")
                        .latitud(new BigDecimal("-31.537500"))
                        .longitud(new BigDecimal("-68.536400"))
                        .capacidadMaxima(300)
                        .costoEstadiaDiaria(18500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito San Luis Logístico")
                        .direccion("Autopista Serranías Puntanas Km 700")
                        .localidad("San Luis")
                        .provincia("San Luis")
                        .latitud(new BigDecimal("-33.301700"))
                        .longitud(new BigDecimal("-66.337800"))
                        .capacidadMaxima(280)
                        .costoEstadiaDiaria(17500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Córdoba Centro")
                        .direccion("Av. Circunvalación 8000")
                        .localidad("Córdoba")
                        .provincia("Córdoba")
                        .latitud(new BigDecimal("-31.416700"))
                        .longitud(new BigDecimal("-64.183300"))
                        .capacidadMaxima(400)
                        .costoEstadiaDiaria(20000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Rosario Norte")
                        .direccion("Av. Circunvalación 12000")
                        .localidad("Rosario")
                        .provincia("Santa Fe")
                        .latitud(new BigDecimal("-32.958700"))
                        .longitud(new BigDecimal("-60.693900"))
                        .capacidadMaxima(380)
                        .costoEstadiaDiaria(19500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Santa Fe Puerto")
                        .direccion("Puerto de Santa Fe")
                        .localidad("Santa Fe")
                        .provincia("Santa Fe")
                        .latitud(new BigDecimal("-31.633300"))
                        .longitud(new BigDecimal("-60.700000"))
                        .capacidadMaxima(260)
                        .costoEstadiaDiaria(17000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Paraná Entre Ríos")
                        .direccion("Costanera Alta")
                        .localidad("Paraná")
                        .provincia("Entre Ríos")
                        .latitud(new BigDecimal("-31.732000"))
                        .longitud(new BigDecimal("-60.528000"))
                        .capacidadMaxima(240)
                        .costoEstadiaDiaria(16500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito CABA Dock Sur")
                        .direccion("Puerto Madero - Dársena")
                        .localidad("Ciudad Autónoma de Buenos Aires")
                        .provincia("CABA")
                        .latitud(new BigDecimal("-34.603700"))
                        .longitud(new BigDecimal("-58.381600"))
                        .capacidadMaxima(500)
                        .costoEstadiaDiaria(25000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito La Plata")
                        .direccion("Zona Puerto La Plata")
                        .localidad("La Plata")
                        .provincia("Buenos Aires")
                        .latitud(new BigDecimal("-34.921400"))
                        .longitud(new BigDecimal("-57.954500"))
                        .capacidadMaxima(320)
                        .costoEstadiaDiaria(19000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Bahía Blanca")
                        .direccion("Zona Portuaria")
                        .localidad("Bahía Blanca")
                        .provincia("Buenos Aires")
                        .latitud(new BigDecimal("-38.718300"))
                        .longitud(new BigDecimal("-62.266300"))
                        .capacidadMaxima(280)
                        .costoEstadiaDiaria(18500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Neuquén")
                        .direccion("Parque Industrial Neuquén")
                        .localidad("Neuquén")
                        .provincia("Neuquén")
                        .latitud(new BigDecimal("-38.951600"))
                        .longitud(new BigDecimal("-68.059100"))
                        .capacidadMaxima(260)
                        .costoEstadiaDiaria(18000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Bariloche")
                        .direccion("Av. 12 de Octubre")
                        .localidad("San Carlos de Bariloche")
                        .provincia("Río Negro")
                        .latitud(new BigDecimal("-41.133500"))
                        .longitud(new BigDecimal("-71.310300"))
                        .capacidadMaxima(220)
                        .costoEstadiaDiaria(18500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Comodoro")
                        .direccion("Zona Industrial Sur")
                        .localidad("Comodoro Rivadavia")
                        .provincia("Chubut")
                        .latitud(new BigDecimal("-45.864100"))
                        .longitud(new BigDecimal("-67.482500"))
                        .capacidadMaxima(240)
                        .costoEstadiaDiaria(19000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Río Gallegos")
                        .direccion("Zona Franca")
                        .localidad("Río Gallegos")
                        .provincia("Santa Cruz")
                        .latitud(new BigDecimal("-51.623000"))
                        .longitud(new BigDecimal("-69.218100"))
                        .capacidadMaxima(200)
                        .costoEstadiaDiaria(20000.0)
                        .build()
        );
        
        depositoRepository.saveAll(depositos);
    }
}
