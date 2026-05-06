package cl.rednorte.ms_portal.controller;

import cl.rednorte.ms_portal.dto.PerfilPacienteRequest;
import cl.rednorte.ms_portal.entity.PerfilPaciente;
import cl.rednorte.ms_portal.repository.PerfilPacienteRepository;
import cl.rednorte.ms_portal.service.PerfilPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/perfil-pacientes")
public class PerfilPacienteController {

    @Autowired
    private PerfilPacienteService perfilPacienteService;

    @Autowired
    private PerfilPacienteRepository perfilPacienteRepository;

    @GetMapping
    public ResponseEntity<List<PerfilPaciente>> listar() {
        return ResponseEntity.ok(perfilPacienteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilPaciente> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(perfilPacienteService.obtenerPorId(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<PerfilPaciente> obtenerPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(perfilPacienteService.obtenerPorPacienteId(pacienteId));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PerfilPacienteRequest req) {
        try {
            PerfilPaciente perfil = perfilPacienteService.crear(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(perfil);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // RF.3 Gestión Perfil: actualizar info de contacto
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody PerfilPacienteRequest req) {
        try {
            return ResponseEntity.ok(perfilPacienteService.actualizar(id, req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        perfilPacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}