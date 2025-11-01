package mp.project.gestionrefugios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Roles {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 50)
  private String nombre;

  @Lob
  @Column(name = "descripcion")
  private String descripcion;

  @ColumnDefault("1")
  @Column(name = "estado_registro", nullable = false)
  private Boolean estadoRegistro = true;

}