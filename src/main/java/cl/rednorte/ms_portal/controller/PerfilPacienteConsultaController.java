package cl.rednorte.ms_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.rednorte.ms_portal.entity.readonly.PerfilPacienteView;
import cl.rednorte.ms_portal.service.PerfilPacienteConsultaService;

@RestController
@RequestMapping("/api/portal/perfil-pacientes")
public class PerfilPacienteConsultaController {
    @Autowired private PerfilPacienteConsultaService service;

    @GetMapping("/auth/{idAuth}")
    public ResponseEntity<PerfilPacienteView> porAuth(@PathVariable String idAuth) { return ResponseEntity.ok(service.porIdAuth(idAuth)); }
}