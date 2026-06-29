package com.vitalpets.citas.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class MascotaClient {

    private final WebClient.Builder webClientBuilder;

    // URL base del MS-Mascotas (puerto 8081)
    private static final String MASCOTAS_URL = "http://localhost:8081";

    /**
     * Verifica si una mascota existe por su ID consultando a MS-Mascotas.
     *
     * @return true si la mascota existe (HTTP 200)
     * @throws RuntimeException con mensaje claro si la mascota no existe (HTTP 404)
     *                          o si MS-Mascotas no está disponible.
     */
    public boolean existeMascota(Long mascotaId) {
        try {
            log.info("Consultando MS-Mascotas para verificar mascota ID: {}", mascotaId);
            webClientBuilder.build()
                    .get()
                    .uri(MASCOTAS_URL + "/api/mascotas/" + mascotaId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Mascota ID: {} verificada exitosamente en MS-Mascotas", mascotaId);
            return true;
        } catch (WebClientResponseException.NotFound e) {
            log.error("MS-Mascotas respondió 404 para mascota ID: {}", mascotaId);
            throw new RuntimeException("Mascota con ID " + mascotaId + " no encontrada");
        } catch (WebClientResponseException e) {
            log.error("MS-Mascotas respondió error HTTP {} para mascota ID: {}",
                    e.getStatusCode(), mascotaId);
            throw new RuntimeException("Error al verificar la mascota con ID " + mascotaId
                    + " (HTTP " + e.getStatusCode().value() + ")");
        } catch (Exception e) {
            log.error("No se pudo contactar a MS-Mascotas para mascota ID: {}. Error: {}",
                    mascotaId, e.getMessage());
            throw new RuntimeException("No se pudo verificar la mascota con ID " + mascotaId
                    + ": MS-Mascotas no está disponible");
        }
    }
}
