package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Donadores;

import java.util.List;
import java.util.Optional;

public interface IDonadoresService {

  public List<Donadores> getDonadores();

  public Optional<Donadores> getDonadorById(Integer id);

  public int saveDonador(Donadores donador);

  public int updateDonador(Donadores donador);

  public void deleteDonador(Integer id);

  public List<Donadores> getDonadoresByStatus(Boolean status);

  public List<Donadores> searchDonadoresByNombre(String nombre);

  public Optional<Donadores> getDonadorByEmail(String email);

  public List<Donadores> searchDonadoresByTelefono(String telefono);

  public List<Donadores> searchDonadoresByDireccion(String direccion);
}