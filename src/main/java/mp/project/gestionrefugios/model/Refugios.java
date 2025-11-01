package mp.project.gestionrefugios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "refugios")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Refugios {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Column(name = "direccion", length = 150)
  private String direccion;

  @ColumnDefault("0")
  @Column(name = "capacidad")
  private Integer capacidad;

  @Column(name = "encargado", length = 100)
  private String encargado;

  @Column(name = "telefono", length = 20)
  private String telefono;

  @ColumnDefault("'Activo'")
  @Column(name = "estado", length = 20)
  private String estado;

  @ColumnDefault("1")
  @Column(name = "estado_registro", nullable = false)
  private Boolean estadoRegistro = true;

}