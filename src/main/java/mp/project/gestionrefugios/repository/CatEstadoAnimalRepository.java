package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.CatEstadoAnimal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CatEstadoAnimalRepository extends JpaRepository<CatEstadoAnimal, Integer> {

  @Query("select c from CatEstadoAnimal c where c.estadoRegistro = :status")
  public List<CatEstadoAnimal> getEstadoAnimalesByStatus(@Param("status") Boolean status);
}
