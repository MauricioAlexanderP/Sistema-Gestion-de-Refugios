package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.TipoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoInventarioRepository extends JpaRepository<TipoInventario, Integer> {
}
