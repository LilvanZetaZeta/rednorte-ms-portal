package cl.rednorte.ms_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.service.ReservaConsultaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaConsultaController {

    @Autowired
    private ReservaConsultaService reservaConsultaService;

    @GetMapping
    public ResponseEntity<List<ReservaView>> listar() {
        return ResponseEntity.ok(reservaConsultaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaView> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorId(id));
    }

    @GetMapping("/paciente/{idAuth}")
    public ResponseEntity<List<ReservaView>> porPacienteIdAuth(@PathVariable String idAuth) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorPacienteIdAuth(idAuth));
    }

    @GetMapping("/paciente/numerico/{pacienteId}")
    public ResponseEntity<List<ReservaView>> porPacienteId(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorPacienteId(pacienteId));
    }

    @GetMapping("/medico/{idAuth}")
    public ResponseEntity<List<ReservaView>> porMedicoIdAuth(@PathVariable String idAuth) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorMedicoIdAuth(idAuth));
    }

    @GetMapping("/medico/numerico/{medicoId}")
    public ResponseEntity<List<ReservaView>> porMedicoId(@PathVariable Long medicoId) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorMedicoId(medicoId));
    }

    @GetMapping("/centro/{centroId}")
    public ResponseEntity<List<ReservaView>> porCentro(@PathVariable Long centroId) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorCentroId(centroId));
    }
}