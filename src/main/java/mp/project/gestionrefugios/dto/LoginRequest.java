package mp.project.gestionrefugios.dto;

public class LoginRequest {
  private String usuario;
  private String contrasena;

  // Constructors
  public LoginRequest() {
  }

  public LoginRequest(String usuario, String contrasena) {
    this.usuario = usuario;
    this.contrasena = contrasena;
  }

  // Getters and Setters
  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public String getContrasena() {
    return contrasena;
  }

  public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
  }
}