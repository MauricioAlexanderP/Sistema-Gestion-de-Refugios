package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Adoptantes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptantesRepository extends JpaRepository<Adoptantes, Integer> {
}
