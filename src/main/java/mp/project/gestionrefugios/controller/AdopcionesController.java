package mp.project.gestionrefugios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mp.project.gestionrefugios.model.Adopciones;
import mp.project.gestionrefugios.services.AdopcionesService;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/adopciones")
@CrossOrigin(origins = "*")
public class AdopcionesController {

  @Autowired
  private AdopcionesService service;


  @GetMapping("/all")
  public List<Adopciones> getAdopciones() {
    return service.getAdopciones();
  }

  @GetMapping("/byEstadoRegistro")
  public List<Adopciones> getAdopcionesByEstadoRegistro(@RequestParam boolean estadoRegistro) {
    return service.getAdopcionesByEstadoRegistro(estadoRegistro);
  }

}
