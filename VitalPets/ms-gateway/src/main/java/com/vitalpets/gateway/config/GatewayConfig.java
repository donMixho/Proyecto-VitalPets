package com.vitalpets.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de enrutamiento del API Gateway (Spring Cloud Gateway).
 *
 * Alojado en http://localhost:8080
 *
 * Cada microservicio se expone bajo el prefijo /api/{recurso}/** y el Gateway
 * reenvía la petición al puerto correspondiente preservando el path completo
 * (stripPrefix(0) no elimina ningún segmento del path).
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // ms-mascotas → 8081
            .route("ms-mascotas", r -> r
                .path("/api/mascotas/**")
                .filters(f -> f.stripPrefix(0))
                .uri("http://localhost:8081"))
            // ms-clientes → 8082
            .route("ms-clientes", r -> r
                .path("/api/clientes/**")
                .filters(f -> f.stripPrefix(0))
                .uri("http://localhost:8082"))
            // ms-citas → 8083
            .route("ms-citas", r -> r
                .path("/api/citas/**")
                .filters(f -> f.stripPrefix(0))
                .uri("http://localhost:8083"))
            // ms-historial → 8084
            .route("ms-historial", r -> r
                .path("/api/historial/**")
                .filters(f -> f.stripPrefix(0))
                .uri("http://localhost:8084"))
            // ms-inventario → 8085
            .route("ms-inventario", r -> r
                .path("/api/productos/**")
                .filters(f -> f.stripPrefix(0))
                .uri("http://localhost:8085"))
            // ms-facturacion → 8086
            .route("ms-facturacion", r -> r
                .path("/api/facturas/**")
                .filters(f -> f.stripPrefix(0))
                .uri("http://localhost:8086"))
            // ms-personal → 8087
            .route("ms-personal", r -> r
                .path("/api/personal/**")
                .filters(f -> f.stripPrefix(0))
                .uri("http://localhost:8087"))
            // ms-vacunas → 8088
            .route("ms-vacunas", r -> r
                .path("/api/vacunas/**")
                .filters(f -> f.stripPrefix(0))
                .uri("http://localhost:8088"))
            // ms-laboratorio → 8089
            .route("ms-laboratorio", r -> r
                .path("/api/examenes/**")
                .filters(f -> f.stripPrefix(0))
                .uri("http://localhost:8089"))
            // ms-usuarios → 8090
            .route("ms-usuarios", r -> r
                .path("/api/usuarios/**")
                .filters(f -> f.stripPrefix(0))
                .uri("http://localhost:8090"))
            .build();
    }
}
