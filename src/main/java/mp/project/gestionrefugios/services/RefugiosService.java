package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Refugios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mp.project.gestionrefugios.interfaces.IRefugiosService;
import mp.project.gestionrefugios.repository.RefugiosRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RefugiosService implements IRefugiosService {

  @Autowired
  private RefugiosRepository data;

  @Override
  public List<Refugios> getRefugios() {
    return data.findAll();
  }

  @Override
  public Optional<Refugios> getRefugioById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveRefugio(Refugios refugio) {
    data.save(refugio);
    return refugio.getId();
  }

  @Override
  public int updateRefugio(Refugios refugio) {
    // Verificar que el refugio existe antes de actualizarlo
    if (refugio.getId() != null && data.existsById(refugio.getId())) {
      data.save(refugio);
      return refugio.getId();
    }
    return 0;
  }

  @Override
  public void deleteRefugio(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Refugios> refugioOptional = data.findById(id);
    if (refugioOptional.isPresent()) {
      Refugios refugio = refugioOptional.get();
      refugio.setEstadoRegistro(false);
      data.save(refugio);
    }
  }

  @Override
  public List<Refugios> getRefugiosByStatus(Boolean status) {
    return data.getRefugiosByStatus(status);
  }
}