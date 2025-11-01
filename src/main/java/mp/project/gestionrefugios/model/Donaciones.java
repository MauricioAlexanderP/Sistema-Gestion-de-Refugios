package mp.project.gestionrefugios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "donaciones")
public class Donaciones {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "refugio_id", nullable = false)
  private Refugios refugio;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "donador_id", nullable = false)
  private Donadores donador;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tipo_id", nullable = false)
  private TipoDonacion tipo;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "recibido_por", nullable = false)
  private Usuarios recibidoPor;

  @Column(name = "monto", precision = 10, scale = 2)
  private BigDecimal monto;

  @Column(name = "cantidad")
  private Integer cantidad;

  @Column(name = "fecha", nullable = false)
  private LocalDate fecha;

  @Lob
  @Column(name = "observaciones")
  private String observaciones;

  @ColumnDefault("1")
  @Column(name = "estado_registro", nullable = false)
  private Boolean estadoRegistro = true;

}