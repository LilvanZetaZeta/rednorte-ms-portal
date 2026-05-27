package cl.rednorte.ms_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.service.ReservaConsultaService;
import java.util.List;

@RestController
@RequestMapping("/api/portal/reservas")
public class ReservaConsultaController {
    @Autowired private ReservaConsultaService service;

    @GetMapping("/paciente/{idAuth}")
    public List<ReservaView> porPaciente(@PathVariable String idAuth) { return service.porPacienteIdAuth(idAuth); }

    @GetMapping("/centro/{centroId}")
    public List<ReservaView> porCentro(@PathVariable Long centroId) { return service.porCentro(centroId); }

    @GetMapping("/medico/{idAuth}")
    public List<ReservaView> porMedico(@PathVariable String idAuth) { return service.porMedico(idAuth); }
}