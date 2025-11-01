package mp.project.gestionrefugios.model;

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
@Table(name = "inventario")
public class Inventario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tipo_id", nullable = false)
  private TipoInventario tipo;

  @Column(name = "cantidad", nullable = false)
  private Integer cantidad;

  @Column(name = "unidad", length = 20)
  private String unidad;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "refugio_id", nullable = false)
  private Refugios refugio;

  @ColumnDefault("5")
  @Column(name = "stock_minimo")
  private Integer stockMinimo;

  @Column(name = "fecha_ingreso", nullable = false)
  private LocalDate fechaIngreso;

  @ColumnDefault("1")
  @Column(name = "estado_registro", nullable = false)
  private Boolean estadoRegistro = true;

}