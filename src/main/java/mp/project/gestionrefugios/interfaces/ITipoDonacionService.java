package mp.project.gestionrefugios.interfaces;

import mp.project.gestionrefugios.model.TipoDonacion;

import java.util.List;
import java.util.Optional;

public interface ITipoDonacionService {

  public List<TipoDonacion> getTipoDonaciones();

  public Optional<TipoDonacion> getTipoDonacionById(Integer id);

  public int saveTipoDonacion(TipoDonacion tipoDonacion);

  public int updateTipoDonacion(TipoDonacion tipoDonacion);

  public void deleteTipoDonacion(Integer id);

  public List<TipoDonacion> getTipoDonacionByStatus(Boolean status);

  public List<TipoDonacion> searchTipoDonacionByNombre(String nombre);

  public Optional<TipoDonacion> getTipoDonacionByNombreExacto(String nombre);

  public List<TipoDonacion> searchTipoDonacionByDescripcion(String descripcion);
}