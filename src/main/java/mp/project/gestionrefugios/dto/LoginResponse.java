package mp.project.gestionrefugios.dto;

import mp.project.gestionrefugios.model.Usuarios;

public class LoginResponse {
  private boolean success;
  private String message;
  private UsuarioDto usuario;

  // Constructors
  public LoginResponse() {
  }

  public LoginResponse(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  public LoginResponse(boolean success, String message, UsuarioDto usuario) {
    this.success = success;
    this.message = message;
    this.usuario = usuario;
  }

  // Getters and Setters
  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public UsuarioDto getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioDto usuario) {
    this.usuario = usuario;
  }

  // DTO interno para no exponer la contraseña
  public static class UsuarioDto {
    private Integer id;
    private String nombre;
    private String usuario;
    private String email;
    private String rol;
    private Boolean estadoRegistro;

    public UsuarioDto(Usuarios usuario) {
      this.id = usuario.getId();
      this.nombre = usuario.getNombre();
      this.usuario = usuario.getUsuario();
      this.email = usuario.getEmail();
      this.rol = usuario.getRol() != null ? usuario.getRol().getNombre() : null;
      this.estadoRegistro = usuario.getEstadoRegistro();
    }

    // Getters and Setters
    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public String getNombre() {
      return nombre;
    }

    public void setNombre(String nombre) {
      this.nombre = nombre;
    }

    public String getUsuario() {
      return usuario;
    }

    public void setUsuario(String usuario) {
      this.usuario = usuario;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getRol() {
      return rol;
    }

    public void setRol(String rol) {
      this.rol = rol;
    }

    public Boolean getEstadoRegistro() {
      return estadoRegistro;
    }

    public void setEstadoRegistro(Boolean estadoRegistro) {
      this.estadoRegistro = estadoRegistro;
    }
  }
}