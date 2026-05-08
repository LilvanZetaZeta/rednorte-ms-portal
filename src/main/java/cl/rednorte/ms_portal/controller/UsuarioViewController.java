package cl.rednorte.ms_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import cl.rednorte.ms_portal.repository.readonly.UsuarioViewRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioViewController {

    @Autowired
    private UsuarioViewRepository usuarioRepo;

    // Reemplaza GET /usuarios/staff de ms-gestion
    @GetMapping("/staff")
    public ResponseEntity<List<UsuarioView>> getStaff() {
        return ResponseEntity.ok(
            usuarioRepo.findAllExceptRoleNative("PACIENTE")
        );
    }

    // Reemplaza GET /usuarios/medicos/buscar de ms-gestion
    @GetMapping("/medicos/buscar")
    public ResponseEntity<List<UsuarioView>> buscarMedicosPorEspecialidad(
            @RequestParam String especialidad) {
        return ResponseEntity.ok(
            usuarioRepo.findMedicosByEspecialidadNombre(especialidad)
        );
    }

    // Extra: perfil de un usuario específico por idAuth
    @GetMapping("/auth/{idAuth}")
    public ResponseEntity<UsuarioView> getByIdAuth(@PathVariable String idAuth) {
        return usuarioRepo.findByIdAuth(idAuth)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}