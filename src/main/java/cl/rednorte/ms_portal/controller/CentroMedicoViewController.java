package cl.rednorte.ms_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.rednorte.ms_portal.entity.readonly.CentroMedicoView;
import cl.rednorte.ms_portal.repository.readonly.CentroMedicoViewRepository;

@RestController
@RequestMapping("/api/centros-medicos")
public class CentroMedicoViewController {

    @Autowired
    private CentroMedicoViewRepository centroRepo;

    @GetMapping
    public ResponseEntity<List<CentroMedicoView>> listar() {
        return ResponseEntity.ok(centroRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CentroMedicoView> getById(@PathVariable Long id) {
        return centroRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}