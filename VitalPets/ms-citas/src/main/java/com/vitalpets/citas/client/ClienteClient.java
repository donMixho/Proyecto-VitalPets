package com.vitalpets.citas.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteClient {

    private final WebClient.Builder webClientBuilder;

    // URL base del MS-Clientes
    private static final String CLIENTES_URL = "http://vitalpets-clientes:8082";

    // Verifica si un cliente existe por su ID
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
        } catch (Exception e) {
            log.error("Error al consultar MS-Clientes para cliente ID: {}. Error: {}",
                    clienteId, e.getMessage());
            return false;
        }
    }
}