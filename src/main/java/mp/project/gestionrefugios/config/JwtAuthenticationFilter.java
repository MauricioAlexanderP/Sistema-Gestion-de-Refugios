package mp.project.gestionrefugios.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain chain)
      throws ServletException, IOException {

    // Permitir peticiones OPTIONS (preflight) sin procesar JWT
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      chain.doFilter(request, response);
      return;
    }

    final String requestTokenHeader = request.getHeader("Authorization");
    final String requestURI = request.getRequestURI();
    final String method = request.getMethod();

    String username = null;
    String jwtToken = null;

    // Log para depuración (solo en desarrollo)
    logger.debug("Processing request: " + method + " " + requestURI + " - Authorization: " +
        (requestTokenHeader != null ? "Present" : "Missing"));

    // JWT Token está en el formato "Bearer token". Remover palabra Bearer y obtener
    // solo el Token
    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      jwtToken = requestTokenHeader.substring(7);
      try {
        username = jwtUtil.extractUsername(jwtToken);
        logger.debug("Extracted username from token: " + username);
      } catch (Exception e) {
        logger.error("Unable to get JWT Token for request " + method + " " + requestURI + ": " + e.getMessage());
      }
    } else {
      logger.debug("No Authorization header or invalid format for request: " + method + " " + requestURI);
    }

    // Una vez que obtengamos el token, validarlo
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      // Si el token es válido, configurar Spring Security para establecer manualmente
      // la autenticación
      if (jwtUtil.isTokenValid(jwtToken)) {

        String role = jwtUtil.extractRole(jwtToken);
        logger.debug("Token valid. User: " + username + " - Role: " + role);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            username,
            null,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Agregar información adicional al contexto
        request.setAttribute("userId", jwtUtil.extractUserId(jwtToken));
        request.setAttribute("userRole", role);

        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.debug("Authentication set for user: " + username);
      } else {
        logger.warn("Invalid JWT token for user: " + username);
      }
    }

    chain.doFilter(request, response);
  }
}