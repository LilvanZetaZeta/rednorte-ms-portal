package cl.rednorte.ms_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.service.ReservaConsultaService;
import java.util.List;

@RestController
@RequestMapping("/api/portal/reservas")
public class ReservaConsultaController {
    @Autowired
    private ReservaConsultaService service;

    @GetMapping("/paciente/{idAuth}")
    public List<ReservaView> porPaciente(@PathVariable String idAuth) {
        return service.porPacienteIdAuth(idAuth);
    }

    @GetMapping("/centro/{centroId}")
    public Page<ReservaView> porCentro(
            @PathVariable Long centroId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.porCentro(centroId, page, size);
    }

    @GetMapping("/medico/{medicoId}")
    public Page<ReservaView> porMedico(
            @PathVariable Long medicoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.porMedico(medicoId, page, size);
    }
}
