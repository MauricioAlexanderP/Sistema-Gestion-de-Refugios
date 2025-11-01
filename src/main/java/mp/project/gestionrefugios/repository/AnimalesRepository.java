package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Animales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalesRepository extends JpaRepository<Animales, Integer> {
}
