package mp.project.gestionrefugios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // Permitir orígenes específicos (en desarrollo permitimos localhost)
    configuration.setAllowedOriginPatterns(Arrays.asList("*"));

    // Permitir métodos HTTP específicos
    configuration.setAllowedMethods(Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));

    // Permitir headers específicos
    configuration.setAllowedHeaders(Arrays.asList(
        "Authorization",
        "Cache-Control",
        "Content-Type",
        "Access-Control-Allow-Headers",
        "Access-Control-Allow-Origin",
        "Origin",
        "Accept",
        "X-Requested-With"));

    // Permitir credentials (cookies, authorization headers)
    configuration.setAllowCredentials(true);

    // Exponer headers en la respuesta
    configuration.setExposedHeaders(Arrays.asList(
        "Authorization",
        "Access-Control-Allow-Origin",
        "Access-Control-Allow-Credentials"));

    // Tiempo de cache para preflight requests
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}