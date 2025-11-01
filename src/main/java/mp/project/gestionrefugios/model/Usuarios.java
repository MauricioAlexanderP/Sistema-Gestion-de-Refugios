package mp.project.gestionrefugios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Usuarios {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Column(name = "usuario", nullable = false, length = 50)
  private String usuario;

  @Column(name = "contrasena", nullable = false)
  private String contrasena;

  @Column(name = "email", nullable = false, length = 100)
  private String email;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "rol_id", nullable = false)
  private Roles rol;

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "personal_id", nullable = true)
  private Personal personal;

  @ColumnDefault("1")
  @Column(name = "estado_registro", nullable = false)
  private Boolean estadoRegistro = false;

}