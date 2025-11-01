package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Donadores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonadoresRepository extends JpaRepository<Donadores, Integer> {
}
