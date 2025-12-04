package com.tpi.mscatalogo.config;

import com.tpi.mscatalogo.domain.Camion;
import com.tpi.mscatalogo.repository.CamionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CamionDataInitializer {
    private final CamionRepository camionRepository;

    @PostConstruct
    public void init() {
		if (camionRepository.count() > 0) {
		    return;
		}

		List<Camion> camiones = List.of(
			Camion.builder()
				.patente("AA000AA")
				.modelo("Scania R450")
				.capacidadKg(28000.0)
				.volumenM3(88.0)
				.consumoLitrosKm(0.32)
				.costoBaseKm(520.0)
				.empresaTransportista("Transporte Patagonia")
				.transportistaNombre("Carlos")
				.transportistaApellido("Gonzalez")
				.transportistaDni("25678432")
				.transportistaLicencia("B2-12345678")
				.transportistaTelefono("+54 9 11 5555-0001")
				.transportistaEmail("carlos.gonzalez@patagonia.com")
				.build(),
			Camion.builder()
				.patente("AA001AA")
				.modelo("Mercedes-Benz Actros 2645")
				.capacidadKg(26000.0)
				.volumenM3(82.0)
				.consumoLitrosKm(0.30)
				.costoBaseKm(500.0)
				.empresaTransportista("Logistica Federal")
				.transportistaNombre("Miguel")
				.transportistaApellido("Rodriguez")
				.transportistaDni("28456789")
				.transportistaLicencia("B2-23456789")
				.transportistaTelefono("+54 9 11 5555-0002")
				.transportistaEmail("miguel.rodriguez@federal.com")
				.build(),
			Camion.builder()
				.patente("AA002AA")
				.modelo("Volvo FH 460")
				.capacidadKg(28000.0)
				.volumenM3(90.0)
				.consumoLitrosKm(0.315)
				.costoBaseKm(530.0)
				.empresaTransportista("Nordic Cargo")
				.transportistaNombre("Juan")
				.transportistaApellido("Martinez")
				.transportistaDni("30123456")
				.transportistaLicencia("B2-34567890")
				.transportistaTelefono("+54 9 11 5555-0003")
				.transportistaEmail("juan.martinez@nordic.com")
				.build(),
			Camion.builder()
				.patente("AA003AA")
				.modelo("Iveco Hi-Way 480")
				.capacidadKg(24000.0)
				.volumenM3(76.0)
				.consumoLitrosKm(0.305)
				.costoBaseKm(495.0)
				.empresaTransportista("Ruta Sur")
				.transportistaNombre("Pedro")
				.transportistaApellido("Fernandez")
				.transportistaDni("27890123")
				.transportistaLicencia("B2-45678901")
				.transportistaTelefono("+54 9 11 5555-0004")
				.transportistaEmail("pedro.fernandez@rutasur.com")
				.build(),
			Camion.builder()
				.patente("AA004AA")
				.modelo("MAN TGX 26.480")
				.capacidadKg(30000.0)
				.volumenM3(94.0)
				.consumoLitrosKm(0.33)
				.costoBaseKm(540.0)
				.empresaTransportista("Atlantico Cargo")
				.transportistaNombre("Roberto")
				.transportistaApellido("Lopez")
				.transportistaDni("26543210")
				.transportistaLicencia("B2-56789012")
				.transportistaTelefono("+54 9 11 5555-0005")
				.transportistaEmail("roberto.lopez@atlantico.com")
				.build(),
			Camion.builder()
				.patente("AA005AA")
				.modelo("DAF XF 530")
				.capacidadKg(27000.0)
				.volumenM3(85.0)
				.consumoLitrosKm(0.31)
				.costoBaseKm(515.0)
				.empresaTransportista("Pampa Cargo")
				.transportistaNombre("Diego")
				.transportistaApellido("Garcia")
				.transportistaDni("29876543")
				.transportistaLicencia("B2-67890123")
				.transportistaTelefono("+54 9 11 5555-0006")
				.transportistaEmail("diego.garcia@pampa.com")
				.build(),
			Camion.builder()
				.patente("AA006AA")
				.modelo("Kenworth T680")
				.capacidadKg(29000.0)
				.volumenM3(92.0)
				.consumoLitrosKm(0.335)
				.costoBaseKm(545.0)
				.empresaTransportista("Cordoba Freight")
				.transportistaNombre("Marcelo")
				.transportistaApellido("Sanchez")
				.transportistaDni("31234567")
				.transportistaLicencia("B2-78901234")
				.transportistaTelefono("+54 9 351 5555-0007")
				.transportistaEmail("marcelo.sanchez@cordobafreight.com")
				.build(),
			Camion.builder()
				.patente("AA007AA")
				.modelo("Freightliner Cascadia")
				.capacidadKg(26500.0)
				.volumenM3(84.0)
				.consumoLitrosKm(0.325)
				.costoBaseKm(505.0)
				.empresaTransportista("Sur Andino")
				.transportistaNombre("Alejandro")
				.transportistaApellido("Perez")
				.transportistaDni("28765432")
				.transportistaLicencia("B2-89012345")
				.transportistaTelefono("+54 9 261 5555-0008")
				.transportistaEmail("alejandro.perez@surandino.com")
				.build(),
			Camion.builder()
				.patente("AA008AA")
				.modelo("Renault T High 480")
				.capacidadKg(25500.0)
				.volumenM3(80.0)
				.consumoLitrosKm(0.298)
				.costoBaseKm(490.0)
				.empresaTransportista("Patagonian Lines")
				.transportistaNombre("Fernando")
				.transportistaApellido("Diaz")
				.transportistaDni("27654321")
				.transportistaLicencia("B2-90123456")
				.transportistaTelefono("+54 9 299 5555-0009")
				.transportistaEmail("fernando.diaz@patagonianlines.com")
				.build(),
			Camion.builder()
				.patente("AA009AA")
				.modelo("Hino Profia 700")
				.capacidadKg(23000.0)
				.volumenM3(72.0)
				.consumoLitrosKm(0.29)
				.costoBaseKm(470.0)
				.empresaTransportista("Japon Cargo")
				.transportistaNombre("Ricardo")
				.transportistaApellido("Torres")
				.transportistaDni("30987654")
				.transportistaLicencia("B2-01234567")
				.transportistaTelefono("+54 9 11 5555-0010")
				.transportistaEmail("ricardo.torres@japoncargo.com")
				.build(),
			Camion.builder()
				.patente("AA010AA")
				.modelo("Scania S500")
				.capacidadKg(29500.0)
				.volumenM3(93.0)
				.consumoLitrosKm(0.34)
				.costoBaseKm(555.0)
				.empresaTransportista("Global Transport")
				.transportistaNombre("Eduardo")
				.transportistaApellido("Ramirez")
				.transportistaDni("26123789")
				.transportistaLicencia("B2-12340567")
				.transportistaTelefono("+54 9 11 5555-0011")
				.transportistaEmail("eduardo.ramirez@globaltransport.com")
				.build()
		);

		camionRepository.saveAll(camiones);
		
		// Log para verificar que se inicializaron los camiones
		System.out.println("✓ Camiones inicializados: " + camionRepository.count());
    }
}



