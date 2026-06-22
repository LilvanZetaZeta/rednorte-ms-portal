package cl.rednorte.ms_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.rednorte.ms_portal.entity.readonly.EspecialidadView;
import cl.rednorte.ms_portal.repository.readonly.EspecialidadViewRepository;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadViewController {

    @Autowired
    private EspecialidadViewRepository especialidadRepo;

    @GetMapping
    public ResponseEntity<List<EspecialidadView>> listar() {
        return ResponseEntity.ok(especialidadRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadView> getById(@PathVariable Long id) {
        return especialidadRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}