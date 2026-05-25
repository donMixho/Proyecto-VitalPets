package com.vitalpets.personal.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioClient {

    private final WebClient.Builder webClientBuilder;
    private static final String USUARIOS_URL = "http://localhost:8090";

    public boolean existeUsuario(Long usuarioId) {
        try {
            log.info("Consultando MS-Usuarios para verificar usuario ID: {}", usuarioId);
            webClientBuilder.build()
                    .get()
                    .uri(USUARIOS_URL + "/api/usuarios/" + usuarioId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Usuario ID: {} verificado en MS-Usuarios", usuarioId);
            return true;
        } catch (Exception e) {
            log.error("Error al consultar MS-Usuarios para usuario ID: {}. Error: {}", usuarioId, e.getMessage());
            return false;
        }
    }
}
