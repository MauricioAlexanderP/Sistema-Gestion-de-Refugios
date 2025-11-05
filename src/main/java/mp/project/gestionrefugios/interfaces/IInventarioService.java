package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.Inventario;

import java.util.List;
import java.util.Optional;

public interface IInventarioService {

  public List<Inventario> getInventarios();

  public Optional<Inventario> getInventarioById(Integer id);

  public int saveInventario(Inventario inventario);

  public int updateInventario(Inventario inventario);

  public void deleteInventario(Integer id);

  public List<Inventario> getInventarioByStatus(Boolean status);

  public List<Inventario> searchInventarioByNombre(String nombre);

  public Optional<Inventario> getInventarioByNombreExacto(String nombre);

  public List<Inventario> getInventarioByTipo(Integer tipoId);

  public List<Inventario> getInventarioByRefugio(Integer refugioId);

  public List<Inventario> getInventarioByTipoAndRefugio(Integer tipoId, Integer refugioId);

  // Métodos específicos para stock mínimo
  public List<Inventario> getInventarioBajoStockMinimo();

  public List<Inventario> getInventarioBajoStockMinimoByRefugio(Integer refugioId);

  public List<Inventario> getInventarioConStockMenorA(Integer cantidad);

  public List<Inventario> getInventarioEnStockCritico(Integer refugioId);
}