package com.vitalpets.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de enrutamiento del API Gateway (Spring Cloud Gateway).
 *
 * Alojado en http://localhost:8080
 *
 * Las URIs de cada microservicio NO están hardcodeadas: se leen de las
 * propiedades vitalpets.services.* (definidas en application.yml con valores
 * localhost para ejecución local). En Docker, esas propiedades se sobrescriben
 * con las variables de entorno VITALPETS_SERVICES_* del docker-compose.yml
 * (nombres de contenedor). Spring Boot prioriza las variables de entorno.
 *
 * Cada ruta usa stripPrefix(0) para preservar el path completo.
 */
@Configuration
public class GatewayConfig {

    @Value("${vitalpets.services.mascotas}")    private String urlMascotas;
    @Value("${vitalpets.services.clientes}")    private String urlClientes;
    @Value("${vitalpets.services.citas}")       private String urlCitas;
    @Value("${vitalpets.services.historial}")   private String urlHistorial;
    @Value("${vitalpets.services.inventario}")  private String urlInventario;
    @Value("${vitalpets.services.facturacion}") private String urlFacturacion;
    @Value("${vitalpets.services.personal}")    private String urlPersonal;
    @Value("${vitalpets.services.vacunas}")     private String urlVacunas;
    @Value("${vitalpets.services.laboratorio}") private String urlLaboratorio;
    @Value("${vitalpets.services.usuarios}")    private String urlUsuarios;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // ms-mascotas → 8081
            .route("ms-mascotas", r -> r
                .path("/api/mascotas/**")
                .filters(f -> f.stripPrefix(0))
                .uri(urlMascotas))
            // ms-clientes → 8082
            .route("ms-clientes", r -> r
                .path("/api/clientes/**")
                .filters(f -> f.stripPrefix(0))
                .uri(urlClientes))
            // ms-citas → 8083
            .route("ms-citas", r -> r
                .path("/api/citas/**")
                .filters(f -> f.stripPrefix(0))
                .uri(urlCitas))
            // ms-historial → 8084
            .route("ms-historial", r -> r
                .path("/api/historial/**")
                .filters(f -> f.stripPrefix(0))
                .uri(urlHistorial))
            // ms-inventario → 8085
            .route("ms-inventario", r -> r
                .path("/api/productos/**")
                .filters(f -> f.stripPrefix(0))
                .uri(urlInventario))
            // ms-facturacion → 8086
            .route("ms-facturacion", r -> r
                .path("/api/facturas/**")
                .filters(f -> f.stripPrefix(0))
                .uri(urlFacturacion))
            // ms-personal → 8087
            .route("ms-personal", r -> r
                .path("/api/personal/**")
                .filters(f -> f.stripPrefix(0))
                .uri(urlPersonal))
            // ms-vacunas → 8088
            .route("ms-vacunas", r -> r
                .path("/api/vacunas/**")
                .filters(f -> f.stripPrefix(0))
                .uri(urlVacunas))
            // ms-laboratorio → 8089
            .route("ms-laboratorio", r -> r
                .path("/api/examenes/**")
                .filters(f -> f.stripPrefix(0))
                .uri(urlLaboratorio))
            // ms-usuarios → 8090
            .route("ms-usuarios", r -> r
                .path("/api/usuarios/**")
                .filters(f -> f.stripPrefix(0))
                .uri(urlUsuarios))
            .build();
    }
}
