package mp.project.gestionrefugios.repository;

import mp.project.gestionrefugios.model.Donaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonacionesRepository extends JpaRepository<Donaciones, Integer> {
}
