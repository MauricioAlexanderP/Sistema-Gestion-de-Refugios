package mp.project.gestionrefugios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "donadores")
public class Donadores {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Column(name = "telefono", length = 20)
  private String telefono;

  @Column(name = "email", length = 100)
  private String email;

  @Column(name = "direccion", length = 150)
  private String direccion;

  @ColumnDefault("1")
  @Column(name = "estado_registro", nullable = false)
  private Boolean estadoRegistro = true;

}