package mp.project.gestionrefugios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "animales")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Animales {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Column(name = "sexo", length = 10)
  private String sexo;

  @Column(name = "edad")
  private Integer edad;

  @Column(name = "fecha_ingreso", nullable = false)
  private LocalDate fechaIngreso;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "especie_id", nullable = false)
  private Especies especie;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "raza_id", nullable = false)
  private Razas raza;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "estado_id", nullable = false)
  private CatEstadoAnimal estado;

  @Column(name = "imagen")
  private String imagen;

  @Lob
  @Column(name = "observaciones")
  private String observaciones;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "refugio_id", nullable = false)
  private Refugios refugio;

  @ColumnDefault("1")
  @Column(name = "estado_registro", nullable = false)
  private Boolean estadoRegistro = true;

}