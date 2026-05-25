package com.vitalpets.facturacion.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class CitaClient {

    private final WebClient.Builder webClientBuilder;
    private static final String CITAS_URL = "http://localhost:8083";

    public boolean existeCita(Long citaId) {
        try {
            log.info("Consultando MS-Citas para verificar cita ID: {}", citaId);
            webClientBuilder.build()
                    .get()
                    .uri(CITAS_URL + "/api/citas/" + citaId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Cita ID: {} verificada exitosamente en MS-Citas", citaId);
            return true;
        } catch (Exception e) {
            log.error("Error al consultar MS-Citas para cita ID: {}. Error: {}", citaId, e.getMessage());
            return false;
        }
    }
}
