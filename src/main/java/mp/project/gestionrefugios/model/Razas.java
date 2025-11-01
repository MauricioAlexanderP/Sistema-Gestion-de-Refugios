package mp.project.gestionrefugios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "razas")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Razas {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 50)
  private String nombre;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "especie_id", nullable = false)
  private Especies especie;

  @ColumnDefault("1")
  @Column(name = "estado_registro", nullable = false)
  private Boolean estadoRegistro = true;

}