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

    @GetMapping("/todos")
    public ResponseEntity<List<UsuarioView>> getTodos() {
        return ResponseEntity.ok(usuarioRepo.findAll());
    }

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

        // Validar si vienen ambos IDs para la búsqueda cruzada
        if (centroId != null && especialidadId != null) {
            return ResponseEntity.ok(usuarioRepo.findMedicosByCentroAndEspecialidad(centroId, especialidadId));
        }

        // Validar si viene el nombre de la especialidad
        if (especialidad != null && !especialidad.trim().isEmpty()) {
            return ResponseEntity.ok(usuarioRepo.findMedicosByEspecialidadNombre(especialidad));
        }

        // Si el usuario no mandó parámetros válidos, rechazar la solicitud en lugar de mandar nulls a la BD
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/auth/{idAuth}")
    public ResponseEntity<UsuarioView> getByIdAuth(@PathVariable String idAuth) {
        return usuarioRepo.findByIdAuth(idAuth)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}