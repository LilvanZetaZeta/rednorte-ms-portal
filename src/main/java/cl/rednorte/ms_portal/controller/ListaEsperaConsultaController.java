package cl.rednorte.ms_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.rednorte.ms_portal.entity.readonly.ListaEsperaLocalView;
import cl.rednorte.ms_portal.service.ListaEsperaConsultaService;
import java.util.List;

@RestController
@RequestMapping("/api/portal/lista-espera")
public class ListaEsperaConsultaController {
    @Autowired private ListaEsperaConsultaService service;

    @GetMapping public List<ListaEsperaLocalView> getAll() { return service.listarTodas(); }
    @GetMapping("/{id}") public ResponseEntity<ListaEsperaLocalView> getById(@PathVariable Long id) { return ResponseEntity.ok(service.obtenerPorId(id)); }
    @GetMapping("/centro/{centroId}") public List<ListaEsperaLocalView> porCentro(@PathVariable Long centroId) { return service.obtenerPorCentro(centroId); }
    @GetMapping("/paciente/{pacienteId}") public List<ListaEsperaLocalView> porPaciente(@PathVariable Long pacienteId) { return service.obtenerPorPaciente(pacienteId); }
}