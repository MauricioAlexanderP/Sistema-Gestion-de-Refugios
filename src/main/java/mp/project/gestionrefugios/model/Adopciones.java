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
@Table(name = "adopciones")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Adopciones {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "animal_id", nullable = false)
  private Animales animal;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "adoptante_id", nullable = false)
  private Adoptantes adoptante;

  @Column(name = "fecha_solicitud", nullable = false)
  private LocalDate fechaSolicitud;

  @Column(name = "fecha_aprobacion")
  private LocalDate fechaAprobacion;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "estado_id", nullable = false)
  private CatEstadoAdopcion estado;

  @Lob
  @Column(name = "observaciones")
  private String observaciones;

  @ColumnDefault("1")
  @Column(name = "estado_registro", nullable = false)
  private Boolean estadoRegistro = true;

}