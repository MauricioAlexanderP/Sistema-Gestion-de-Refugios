package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.interfaces.IAdopcionesService;
import mp.project.gestionrefugios.model.Adopciones;
import mp.project.gestionrefugios.repository.AdopcionesRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdopcionesService implements IAdopcionesService{

  @Autowired
  private AdopcionesRepository data;

  @Override
  public List<Adopciones> getAdopciones() {
    return data.findAll();
  }

  @Override
  public Optional<Adopciones> getAdopcionByID(int id) {
    return Optional.empty();
  }

  @Override
  public int saveAdopciones(Adopciones adopciones) {
    Adopciones saved = data.save(adopciones);
    if (saved == null) {
      throw new IllegalStateException("Error" + adopciones);
    }
    return saved.getId();
  }

  @Override
  public int updateAdopciones(Adopciones adopciones) {
    Adopciones updated = data.save(adopciones);
    if (updated == null) {
      throw new IllegalStateException("Error" + adopciones);
    }
    return updated.getId();
  }

  @Override
  public void deleteAdopciones(int id) {
    data.deleteById(id);
  }

  @Override
  public List<Adopciones> getAdopcionesByEstadoRegistro(boolean b) {
    return data.findByEstadoRegistro(b);
  }
}
