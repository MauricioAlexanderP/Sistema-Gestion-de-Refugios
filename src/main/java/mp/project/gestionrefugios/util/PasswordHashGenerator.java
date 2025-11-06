package mp.project.gestionrefugios.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para generar hashes BCrypt de contraseñas
 * Usado principalmente para generar contraseñas para usuarios de prueba
 */
public class PasswordHashGenerator {

  public static void main(String[] args) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Generar hashes para usuarios de prueba
    String adminPassword = "admin123";
    String gerentePassword = "gerente123";

    String adminHash = encoder.encode(adminPassword);
    String gerenteHash = encoder.encode(gerentePassword);

    System.out.println("=== HASHES DE CONTRASEÑAS ===");
    System.out.println("Admin (admin123): " + adminHash);
    System.out.println("Gerente (gerente123): " + gerenteHash);
    System.out.println();

    // SQL para insertar usuarios
    System.out.println("=== SQL DE INSERCIÓN ===");
    System.out.println("-- Usuario Admin");
    System.out.printf(
        "INSERT INTO usuarios (nombre, usuario, contrasena, email, rol_id, estado_registro) VALUES ('Administrador Principal', 'admin', '%s', 'admin@refugio.com', 1, true);%n",
        adminHash);
    System.out.println();
    System.out.println("-- Usuario Gerente");
    System.out.printf(
        "INSERT INTO usuarios (nombre, usuario, contrasena, email, rol_id, estado_registro) VALUES ('Gerente de Prueba', 'gerente', '%s', 'gerente@refugio.com', 2, true);%n",
        gerenteHash);

    // Verificar que los hashes funcionan
    System.out.println();
    System.out.println("=== VERIFICACIÓN ===");
    System.out.println("Admin password matches: " + encoder.matches(adminPassword, adminHash));
    System.out.println("Gerente password matches: " + encoder.matches(gerentePassword, gerenteHash));
  }
}