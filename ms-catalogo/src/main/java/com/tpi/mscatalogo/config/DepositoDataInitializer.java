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
                        .build(),
                
                Deposito.builder()
                        .nombre("Depósito Salta Sur")
                        .direccion("Ruta Nacional 68 Km 5")
                        .localidad("Salta")
                        .provincia("Salta")
                        .latitud(new BigDecimal("-24.833300"))
                        .longitud(new BigDecimal("-65.416700"))
                        .capacidadMaxima(280)
                        .costoEstadiaDiaria(17500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Tucumán Norte")
                        .direccion("Ruta 9 Km 1240")
                        .localidad("Yerba Buena")
                        .provincia("Tucumán")
                        .latitud(new BigDecimal("-26.809900"))
                        .longitud(new BigDecimal("-65.329400"))
                        .capacidadMaxima(260)
                        .costoEstadiaDiaria(17000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Jujuy Quebrada")
                        .direccion("Parque Industrial Alto Comedero")
                        .localidad("San Salvador de Jujuy")
                        .provincia("Jujuy")
                        .latitud(new BigDecimal("-24.233300"))
                        .longitud(new BigDecimal("-65.266700"))
                        .capacidadMaxima(280)
                        .costoEstadiaDiaria(17500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Formosa Capital")
                        .direccion("Av. Gobernador Gutnisky 3800")
                        .localidad("Formosa")
                        .provincia("Formosa")
                        .latitud(new BigDecimal("-26.189700"))
                        .longitud(new BigDecimal("-58.228900"))
                        .capacidadMaxima(240)
                        .costoEstadiaDiaria(16500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Santiago del Estero Centro")
                        .direccion("Av. Belgrano Sur 3000")
                        .localidad("Santiago del Estero")
                        .provincia("Santiago del Estero")
                        .latitud(new BigDecimal("-27.783300"))
                        .longitud(new BigDecimal("-64.266700"))
                        .capacidadMaxima(260)
                        .costoEstadiaDiaria(17000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Catamarca Valle")
                        .direccion("Ruta Provincial 33 Km 8")
                        .localidad("San Fernando del Valle de Catamarca")
                        .provincia("Catamarca")
                        .latitud(new BigDecimal("-28.469600"))
                        .longitud(new BigDecimal("-65.785200"))
                        .capacidadMaxima(220)
                        .costoEstadiaDiaria(16000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito La Rioja Centro")
                        .direccion("Ruta 38 Km 5")
                        .localidad("La Rioja")
                        .provincia("La Rioja")
                        .latitud(new BigDecimal("-29.413100"))
                        .longitud(new BigDecimal("-66.855800"))
                        .capacidadMaxima(240)
                        .costoEstadiaDiaria(16500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Córdoba Norte")
                        .direccion("Av. Japón 4200")
                        .localidad("Córdoba")
                        .provincia("Córdoba")
                        .latitud(new BigDecimal("-31.350000"))
                        .longitud(new BigDecimal("-64.200000"))
                        .capacidadMaxima(350)
                        .costoEstadiaDiaria(19500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Córdoba Sur")
                        .direccion("Ruta 5 Km 12")
                        .localidad("Córdoba")
                        .provincia("Córdoba")
                        .latitud(new BigDecimal("-31.500000"))
                        .longitud(new BigDecimal("-64.250000"))
                        .capacidadMaxima(340)
                        .costoEstadiaDiaria(19000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Rosario Sur")
                        .direccion("Circunvalación Sur Km 8")
                        .localidad("Rosario")
                        .provincia("Santa Fe")
                        .latitud(new BigDecimal("-33.000000"))
                        .longitud(new BigDecimal("-60.650000"))
                        .capacidadMaxima(360)
                        .costoEstadiaDiaria(18500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Santa Fe Norte")
                        .direccion("Ruta 11 Km 483")
                        .localidad("Santa Fe")
                        .provincia("Santa Fe")
                        .latitud(new BigDecimal("-31.600000"))
                        .longitud(new BigDecimal("-60.720000"))
                        .capacidadMaxima(250)
                        .costoEstadiaDiaria(16500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Paraná Sur")
                        .direccion("Ruta 12 Km 8")
                        .localidad("Paraná")
                        .provincia("Entre Ríos")
                        .latitud(new BigDecimal("-31.800000"))
                        .longitud(new BigDecimal("-60.520000"))
                        .capacidadMaxima(230)
                        .costoEstadiaDiaria(16000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Mar del Plata Puerto")
                        .direccion("Zona Portuaria")
                        .localidad("Mar del Plata")
                        .provincia("Buenos Aires")
                        .latitud(new BigDecimal("-38.000000"))
                        .longitud(new BigDecimal("-57.550000"))
                        .capacidadMaxima(320)
                        .costoEstadiaDiaria(18500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Olavarría")
                        .direccion("Parque Industrial La Esperanza")
                        .localidad("Olavarría")
                        .provincia("Buenos Aires")
                        .latitud(new BigDecimal("-36.900000"))
                        .longitud(new BigDecimal("-60.350000"))
                        .capacidadMaxima(260)
                        .costoEstadiaDiaria(17000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito San Rafael")
                        .direccion("Ruta 143 Km 5")
                        .localidad("San Rafael")
                        .provincia("Mendoza")
                        .latitud(new BigDecimal("-34.600000"))
                        .longitud(new BigDecimal("-68.333300"))
                        .capacidadMaxima(250)
                        .costoEstadiaDiaria(17500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Villa Mercedes")
                        .direccion("Zona Industrial Este")
                        .localidad("Villa Mercedes")
                        .provincia("San Luis")
                        .latitud(new BigDecimal("-33.700000"))
                        .longitud(new BigDecimal("-65.450000"))
                        .capacidadMaxima(240)
                        .costoEstadiaDiaria(17000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Río Cuarto")
                        .direccion("Parque Industrial Río Cuarto")
                        .localidad("Río Cuarto")
                        .provincia("Córdoba")
                        .latitud(new BigDecimal("-33.130000"))
                        .longitud(new BigDecimal("-64.340000"))
                        .capacidadMaxima(300)
                        .costoEstadiaDiaria(18500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Ushuaia")
                        .direccion("Zona Franca Ushuaia")
                        .localidad("Ushuaia")
                        .provincia("Tierra del Fuego")
                        .latitud(new BigDecimal("-54.801900"))
                        .longitud(new BigDecimal("-68.302900"))
                        .capacidadMaxima(180)
                        .costoEstadiaDiaria(21000.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Río Grande")
                        .direccion("Ruta 3 Km 2850")
                        .localidad("Río Grande")
                        .provincia("Tierra del Fuego")
                        .latitud(new BigDecimal("-53.787700"))
                        .longitud(new BigDecimal("-67.709000"))
                        .capacidadMaxima(200)
                        .costoEstadiaDiaria(20500.0)
                        .build(),

                Deposito.builder()
                        .nombre("Depósito Trelew")
                        .direccion("Parque Industrial Trelew")
                        .localidad("Trelew")
                        .provincia("Chubut")
                        .latitud(new BigDecimal("-43.248900"))
                        .longitud(new BigDecimal("-65.305100"))
                        .capacidadMaxima(230)
                        .costoEstadiaDiaria(18000.0)
                        .build()

        );
        
        depositoRepository.saveAll(depositos);
    }
}
