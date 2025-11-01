package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.TipoDonacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDonacionRepository extends JpaRepository<TipoDonacion, Integer> {

}
