package cl.rednorte.ms_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.rednorte.ms_portal.entity.readonly.CentroMedicoView;
import cl.rednorte.ms_portal.service.CentroMedicoConsultaService;
import java.util.List;

@RestController
@RequestMapping("/api/portal/centros-medicos")
public class CentroMedicoConsultaController {
    @Autowired private CentroMedicoConsultaService service;

    @GetMapping
    public List<CentroMedicoView> listar() { return service.listarTodos(); }

    @GetMapping("/buscar")
    public ResponseEntity<List<CentroMedicoView>> buscar(@RequestParam(required = false) String region, @RequestParam(required = false) String comuna) {
        return ResponseEntity.ok(service.buscarPorLocalizacion(region, comuna));
    }
}