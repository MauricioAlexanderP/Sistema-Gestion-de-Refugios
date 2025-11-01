package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Especies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mp.project.gestionrefugios.interfaces.IEspecieService;
import mp.project.gestionrefugios.repository.EspeciesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EspeciesService implements IEspecieService {

  @Autowired
  private EspeciesRepository data;

  @Override
  public List<Especies> getEspecies() {
    return data.findAll();
  }

  @Override
  public Optional<Especies> getEspecieById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveEspecie(Especies especie) {
    data.save(especie);
    return especie.getId();
  }

  @Override
  public int updateEspecie(Especies especie) {
    // Verificar que la especie existe antes de actualizarla
    if (especie.getId() != null && data.existsById(especie.getId())) {
      data.save(especie);
      return especie.getId();
    }
    return 0;
  }

  @Override
  public void deleteEspecie(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Especies> especieOptional = data.findById(id);
    if (especieOptional.isPresent()) {
      Especies especie = especieOptional.get();
      especie.setEstadoRegistro(false);
      data.save(especie);
    }
  }

  @Override
  public List<Especies> getEspeciesByStatus(Boolean status) {
    return data.getEspeciesByStatus(status);
  }
}