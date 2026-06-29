package com.vitalpets.citas.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteClient {

    private final WebClient.Builder webClientBuilder;

    // URL base del MS-Clientes (puerto 8082)
    private static final String CLIENTES_URL = "http://localhost:8082";

    /**
     * Verifica si un cliente existe por su ID consultando a MS-Clientes.
     *
     * @return true si el cliente existe (HTTP 200)
     * @throws RuntimeException con mensaje claro si el cliente no existe (HTTP 404)
     *                          o si MS-Clientes no está disponible.
     */
    public boolean existeCliente(Long clienteId) {
        try {
            log.info("Consultando MS-Clientes para verificar cliente ID: {}", clienteId);
            webClientBuilder.build()
                    .get()
                    .uri(CLIENTES_URL + "/api/clientes/" + clienteId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Cliente ID: {} verificado exitosamente en MS-Clientes", clienteId);
            return true;
        } catch (WebClientResponseException.NotFound e) {
            log.error("MS-Clientes respondió 404 para cliente ID: {}", clienteId);
            throw new RuntimeException("Cliente con ID " + clienteId + " no encontrado");
        } catch (WebClientResponseException e) {
            log.error("MS-Clientes respondió error HTTP {} para cliente ID: {}",
                    e.getStatusCode(), clienteId);
            throw new RuntimeException("Error al verificar el cliente con ID " + clienteId
                    + " (HTTP " + e.getStatusCode().value() + ")");
        } catch (Exception e) {
            log.error("No se pudo contactar a MS-Clientes para cliente ID: {}. Error: {}",
                    clienteId, e.getMessage());
            throw new RuntimeException("No se pudo verificar el cliente con ID " + clienteId
                    + ": MS-Clientes no está disponible");
        }
    }
}
