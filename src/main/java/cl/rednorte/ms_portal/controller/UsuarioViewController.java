package cl.rednorte.ms_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import cl.rednorte.ms_portal.repository.readonly.UsuarioViewRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioViewController {

    @Autowired
    private UsuarioViewRepository usuarioRepo;

    @GetMapping("/staff")
    public ResponseEntity<List<UsuarioView>> getStaff() {
        return ResponseEntity.ok(
            usuarioRepo.findAllExceptRoleNative("PACIENTE")
        );
    }

    @GetMapping("/medicos/buscar")
    public ResponseEntity<List<UsuarioView>> buscarMedicos(
            @RequestParam(required = false) String especialidad,
            @RequestParam(required = false) Long centroId,
            @RequestParam(required = false) Long especialidadId) {
        
        if (centroId != null && especialidadId != null) {
            return ResponseEntity.ok(usuarioRepo.findMedicosByCentroAndEspecialidad(centroId, especialidadId));
        }
        
        return ResponseEntity.ok(usuarioRepo.findMedicosByEspecialidadNombre(especialidad));
    }

    @GetMapping("/auth/{idAuth}")
    public ResponseEntity<UsuarioView> getByIdAuth(@PathVariable String idAuth) {
        return usuarioRepo.findByIdAuth(idAuth)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}