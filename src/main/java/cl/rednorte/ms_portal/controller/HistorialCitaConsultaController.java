package cl.rednorte.ms_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cl.rednorte.ms_portal.entity.readonly.HistorialCitaView;
import cl.rednorte.ms_portal.service.HistorialCitaConsultaService;
import java.util.List;

@RestController
@RequestMapping("/api/portal/historial-citas")
public class HistorialCitaConsultaController {
    @Autowired private HistorialCitaConsultaService service;

    @GetMapping("/paciente/{pacienteId}")
    public List<HistorialCitaView> porPaciente(@PathVariable Long pacienteId) { return service.porPaciente(pacienteId); }
}