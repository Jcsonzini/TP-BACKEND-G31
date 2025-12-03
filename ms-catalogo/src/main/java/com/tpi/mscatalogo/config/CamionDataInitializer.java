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
				.build(),
			Camion.builder()
				.patente("AA001AA")
				.modelo("Mercedes-Benz Actros 2645")
				.capacidadKg(26000.0)
				.volumenM3(82.0)
				.consumoLitrosKm(0.30)
				.costoBaseKm(500.0)
				.empresaTransportista("Logistica Federal")
				.build(),
			Camion.builder()
				.patente("AA002AA")
				.modelo("Volvo FH 460")
				.capacidadKg(28000.0)
				.volumenM3(90.0)
				.consumoLitrosKm(0.315)
				.costoBaseKm(530.0)
				.empresaTransportista("Nordic Cargo")
				.build(),
			Camion.builder()
				.patente("AA003AA")
				.modelo("Iveco Hi-Way 480")
				.capacidadKg(24000.0)
				.volumenM3(76.0)
				.consumoLitrosKm(0.305)
				.costoBaseKm(495.0)
				.empresaTransportista("Ruta Sur")
				.build(),
			Camion.builder()
				.patente("AA004AA")
				.modelo("MAN TGX 26.480")
				.capacidadKg(30000.0)
				.volumenM3(94.0)
				.consumoLitrosKm(0.33)
				.costoBaseKm(540.0)
				.empresaTransportista("Atlantico Cargo")
				.build(),
			Camion.builder()
				.patente("AA005AA")
				.modelo("DAF XF 530")
				.capacidadKg(27000.0)
				.volumenM3(85.0)
				.consumoLitrosKm(0.31)
				.costoBaseKm(515.0)
				.empresaTransportista("Pampa Cargo")
				.build(),
			Camion.builder()
				.patente("AA006AA")
				.modelo("Kenworth T680")
				.capacidadKg(29000.0)
				.volumenM3(92.0)
				.consumoLitrosKm(0.335)
				.costoBaseKm(545.0)
				.empresaTransportista("Cordoba Freight")
				.build(),
			Camion.builder()
				.patente("AA007AA")
				.modelo("Freightliner Cascadia")
				.capacidadKg(26500.0)
				.volumenM3(84.0)
				.consumoLitrosKm(0.325)
				.costoBaseKm(505.0)
				.empresaTransportista("Sur Andino")
				.build(),
			Camion.builder()
				.patente("AA008AA")
				.modelo("Renault T High 480")
				.capacidadKg(25500.0)
				.volumenM3(80.0)
				.consumoLitrosKm(0.298)
				.costoBaseKm(490.0)
				.empresaTransportista("Patagonian Lines")
				.build(),
			Camion.builder()
				.patente("AA009AA")
				.modelo("Hino Profia 700")
				.capacidadKg(23000.0)
				.volumenM3(72.0)
				.consumoLitrosKm(0.29)
				.costoBaseKm(470.0)
				.empresaTransportista("Japon Cargo")
				.build(),
			Camion.builder()
				.patente("AA010AA")
				.modelo("Scania S500")
				.capacidadKg(29500.0)
				.volumenM3(93.0)
				.consumoLitrosKm(0.34)
				.costoBaseKm(555.0)
				.empresaTransportista("Global Transport")
				.build()
		);

		camionRepository.saveAll(camiones);
    }
}



