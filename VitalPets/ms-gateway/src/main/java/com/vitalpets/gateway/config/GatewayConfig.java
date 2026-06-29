package com.vitalpets.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    //alojado en el puerto http://localhost:8080
    //http://localhost:8000/docs
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("ms-mascotas",
                r -> r.path("/api/mascotas/**")
                      .uri("http://localhost:8081"))
            .route("ms-clientes",
                r -> r.path("/api/clientes/**")
                      .uri("http://localhost:8082"))
            .route("ms-citas",
                r -> r.path("/api/citas/**")
                      .uri("http://localhost:8083"))
            .route("ms-historial",
                r -> r.path("/api/historial/**")
                      .uri("http://localhost:8084"))
            .route("ms-inventario",
                r -> r.path("/api/productos/**")
                      .uri("http://localhost:8085"))
            .route("ms-facturacion",
                r -> r.path("/api/facturas/**")
                      .uri("http://localhost:8086"))
            .route("ms-personal",
                r -> r.path("/api/personal/**")
                      .uri("http://localhost:8087"))
            .route("ms-vacunas",
                r -> r.path("/api/vacunas/**")
                      .uri("http://localhost:8088"))
            .route("ms-laboratorio",
                r -> r.path("/api/examenes/**")
                      .uri("http://localhost:8089"))
            .route("ms-usuarios",
                r -> r.path("/api/usuarios/**")
                      .uri("http://localhost:8090"))
            .build();
    }
}
