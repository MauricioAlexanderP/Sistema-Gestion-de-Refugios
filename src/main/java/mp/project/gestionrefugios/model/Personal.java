package mp.project.gestionrefugios.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "personal")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Personal {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cargo_id", nullable = false)
  private Cargo cargo;

  @Column(name = "telefono", length = 20)
  private String telefono;

  @Column(name = "email", length = 100)
  private String email;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "refugio_id", nullable = false)
  private Refugios refugio;

  @ColumnDefault("1")
  @Column(name = "estado_registro", nullable = false)
  private Boolean estadoRegistro = true;

}