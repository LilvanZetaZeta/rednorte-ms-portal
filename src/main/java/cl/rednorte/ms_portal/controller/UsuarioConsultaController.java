package cl.rednorte.ms_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import cl.rednorte.ms_portal.service.UsuarioConsultaService;
import java.util.List;

@RestController
@RequestMapping("/api/portal/usuarios")
public class UsuarioConsultaController {
    @Autowired private UsuarioConsultaService service;

    @GetMapping public List<UsuarioView> listar() { return service.listarTodos(); }
    @GetMapping("/staff") public List<UsuarioView> listarStaff() { return service.listarPersonalStaff(); }
    @GetMapping("/admins-disponibles") public List<UsuarioView> adminsDisponibles() { return service.listarAdministradoresDisponibles(); }
    @GetMapping("/{id}") public ResponseEntity<UsuarioView> getById(@PathVariable Long id) { return ResponseEntity.ok(service.obtenerPorId(id)); }
    @GetMapping("/perfil/{idAuth}") public ResponseEntity<UsuarioView> getPerfil(@PathVariable String idAuth) { return ResponseEntity.ok(service.obtenerPorIdAuth(idAuth)); }
    @GetMapping("/rut/{rut}") public ResponseEntity<UsuarioView> getPorRut(@PathVariable String rut) {
        return service.buscarPorRut(rut)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/medicos/buscar") public List<UsuarioView> buscarMedicos(@RequestParam String especialidad) { return service.buscarMedicosPorEspecialidad(especialidad); }
}