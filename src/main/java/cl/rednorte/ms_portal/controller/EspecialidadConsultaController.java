package cl.rednorte.ms_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cl.rednorte.ms_portal.entity.readonly.EspecialidadView;
import cl.rednorte.ms_portal.service.EspecialidadConsultaService;
import java.util.List;

@RestController
@RequestMapping("/api/portal/especialidades")
public class EspecialidadConsultaController {
    @Autowired private EspecialidadConsultaService service;

    @GetMapping public List<EspecialidadView> getAll() { return service.listar(); }
}