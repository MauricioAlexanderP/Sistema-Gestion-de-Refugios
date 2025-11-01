package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Razas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mp.project.gestionrefugios.interfaces.IRazasService;
import mp.project.gestionrefugios.repository.RazasRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RazasService implements IRazasService {

  @Autowired
  private RazasRepository data;

  @Override
  public List<Razas> getRazas() {
    return data.findAll();
  }

  @Override
  public Optional<Razas> getRazaById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveRaza(Razas raza) {
    data.save(raza);
    return raza.getId();
  }

  @Override
  public int updateRaza(Razas raza) {
    // Verificar que la raza existe antes de actualizarla
    if (raza.getId() != null && data.existsById(raza.getId())) {
      data.save(raza);
      return raza.getId();
    }
    return 0;
  }

  @Override
  public void deleteRaza(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Razas> razaOptional = data.findById(id);
    if (razaOptional.isPresent()) {
      Razas raza = razaOptional.get();
      raza.setEstadoRegistro(false);
      data.save(raza);
    }
  }

  @Override
  public List<Razas> getRazasByStatus(Boolean status) {
    return data.getRazasByStatus(status);
  }

  @Override
  public List<Razas> getRazasByEspecieId(Integer especieId) {
    return data.getRazasByEspecieId(especieId);
  }
}