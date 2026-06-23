package com.vitalpets.laboratorio.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class MascotaClient {

    private final WebClient.Builder webClientBuilder;
    private static final String MASCOTAS_URL = "http://vitalpets-mascotas:8081";

    public boolean existeMascota(Long mascotaId) {
        try {
            log.info("Consultando MS-Mascotas para verificar mascota ID: {}", mascotaId);
            webClientBuilder.build()
                    .get()
                    .uri(MASCOTAS_URL + "/api/mascotas/" + mascotaId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Mascota ID: {} verificada en MS-Mascotas", mascotaId);
            return true;
        } catch (Exception e) {
            log.error("Error al consultar MS-Mascotas para mascota ID: {}. Error: {}", mascotaId, e.getMessage());
            return false;
        }
    }
}
