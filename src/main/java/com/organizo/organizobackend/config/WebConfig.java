package com.organizo.organizobackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class WebConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 1) Origem do seu front
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        // 2) Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 3) Quais cabeçalhos o front pode enviar
        config.setAllowedHeaders(List.of("*"));
        // 4) Se quiser enviar cookies/autenticação
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica a configuração em todas as URLs
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
