package com.vitalpets.historial.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Solo se exige token en operaciones de escritura (POST, PUT, DELETE, PATCH).
        // Las lecturas (GET) y el preflight CORS (OPTIONS) quedan públicas, lo que
        // permite la comunicación interna entre microservicios vía WebClient (solo usa GET).
        String metodo = request.getMethod();
        return "GET".equalsIgnoreCase(metodo) || "OPTIONS".equalsIgnoreCase(metodo);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"error\": \"Token JWT requerido\", \"status\": 401}"
            );
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"error\": \"Token JWT inválido o expirado\", \"status\": 401}"
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}
