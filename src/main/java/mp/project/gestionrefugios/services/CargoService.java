package mp.project.gestionrefugios.services;

import mp.project.gestionrefugios.model.Cargo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mp.project.gestionrefugios.interfaces.ICargoService;
import mp.project.gestionrefugios.repository.CargoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CargoService implements ICargoService {

  @Autowired
  private CargoRepository data;

  @Override
  public List<Cargo> getCargos() {
    return data.findAll();
  }

  @Override
  public Optional<Cargo> getCargoById(Integer id) {
    return data.findById(id);
  }

  @Override
  public int saveCargo(Cargo cargo) {
    data.save(cargo);
    return cargo.getId();
  }

  @Override
  public int updateCargo(Cargo cargo) {
    // Verificar que el cargo existe antes de actualizarlo
    if (cargo.getId() != null && data.existsById(cargo.getId())) {
      data.save(cargo);
      return cargo.getId();
    }
    return 0;
  }

  @Override
  public void deleteCargo(Integer id) {
    // Eliminación lógica: cambiar el estado_registro a false
    Optional<Cargo> cargoOptional = data.findById(id);
    if (cargoOptional.isPresent()) {
      Cargo cargo = cargoOptional.get();
      cargo.setEstadoRegistro(false);
      data.save(cargo);
    }
  }

  @Override
  public List<Cargo> getCargosByStatus(Boolean status) {
    return data.getCargosByStatus(status);
  }
}