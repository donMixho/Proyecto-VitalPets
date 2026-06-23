package com.vitalpets.inventario.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class PersonalClient {

    private final WebClient.Builder webClientBuilder;
    private static final String PERSONAL_URL = "http://vitalpets-personal:8087";

    public boolean existePersonal(Long personalId) {
        try {
            log.info("Consultando MS-Personal para verificar personal ID: {}", personalId);
            webClientBuilder.build()
                    .get()
                    .uri(PERSONAL_URL + "/api/personal/" + personalId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Personal ID: {} verificado en MS-Personal", personalId);
            return true;
        } catch (Exception e) {
            log.error("Error al consultar MS-Personal para personal ID: {}. Error: {}", personalId, e.getMessage());
            return false;
        }
    }
}
