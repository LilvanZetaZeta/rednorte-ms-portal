package cl.rednorte.ms_portal.controller;

import cl.rednorte.ms_portal.dto.HistorialCitaRequest;
import cl.rednorte.ms_portal.entity.HistorialCita;
import cl.rednorte.ms_portal.entity.HistorialCita.EstadoHistorico;
import cl.rednorte.ms_portal.service.HistorialCitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historial-citas")
public class HistorialCitaController {

    @Autowired
    private HistorialCitaService historialCitaService;

    @GetMapping
    public ResponseEntity<List<HistorialCita>> listar() {
        return ResponseEntity.ok(historialCitaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialCita> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(historialCitaService.obtenerPorId(id));
    }

    // RF.2 Historial completo del paciente
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<HistorialCita>> obtenerHistorialPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(historialCitaService.obtenerHistorialPaciente(pacienteId));
    }

    // RF.1 Consulta de estado: solo finalizadas
    @GetMapping("/paciente/{pacienteId}/finalizadas")
    public ResponseEntity<List<HistorialCita>> obtenerFinalizadas(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(
            historialCitaService.obtenerPorEstado(pacienteId, EstadoHistorico.FINALIZADA)
        );
    }

    // RF.1 Consulta de estado: solo canceladas
    @GetMapping("/paciente/{pacienteId}/canceladas")
    public ResponseEntity<List<HistorialCita>> obtenerCanceladas(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(
            historialCitaService.obtenerPorEstado(pacienteId, EstadoHistorico.CANCELADA)
        );
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody HistorialCitaRequest req) {
        try {
            HistorialCita cita = historialCitaService.crear(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(cita);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}