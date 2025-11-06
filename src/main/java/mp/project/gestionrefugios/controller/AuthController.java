package mp.project.gestionrefugios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

  @GetMapping("/profile")
  public ResponseEntity<?> getProfile(HttpServletRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      String username = authentication.getName();
      String role = (String) request.getAttribute("userRole");
      Integer userId = (Integer) request.getAttribute("userId");

      return ResponseEntity.ok(new ProfileResponse(username, role, userId));
    }

    return ResponseEntity.status(401).body("No autenticado");
  }

  @GetMapping("/admin-only")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> adminOnly() {
    return ResponseEntity.ok("Acceso permitido solo para administradores");
  }

  @GetMapping("/admin-or-gerente")
  @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
  public ResponseEntity<String> adminOrGerente() {
    return ResponseEntity.ok("Acceso permitido para administradores y gerentes");
  }

  @GetMapping("/authenticated")
  public ResponseEntity<String> authenticated() {
    return ResponseEntity.ok("Acceso permitido para usuarios autenticados");
  }

  // Clase interna para respuesta del perfil
  public static class ProfileResponse {
    private String username;
    private String role;
    private Integer userId;

    public ProfileResponse(String username, String role, Integer userId) {
      this.username = username;
      this.role = role;
      this.userId = userId;
    }

    // Getters
    public String getUsername() {
      return username;
    }

    public String getRole() {
      return role;
    }

    public Integer getUserId() {
      return userId;
    }
  }
}