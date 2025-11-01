package mp.project.gestionrefugios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "cat_estado_adopcion")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CatEstadoAdopcion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 50)
  private String nombre;

  @ColumnDefault("1")
  @Column(name = "estado_registro", nullable = false)
  private Boolean estadoRegistro = true;

}