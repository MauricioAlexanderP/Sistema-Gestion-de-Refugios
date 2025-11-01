package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Cargo;

import java.util.List;
import java.util.Optional;

public interface ICargoService {

  public List<Cargo> getCargos();

  public Optional<Cargo> getCargoById(Integer id);

  public int saveCargo(Cargo cargo);

  public int updateCargo(Cargo cargo);

  public void deleteCargo(Integer id);

  public List<Cargo> getCargosByStatus(Boolean status);
}