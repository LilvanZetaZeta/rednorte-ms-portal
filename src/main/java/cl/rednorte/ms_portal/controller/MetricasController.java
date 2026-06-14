package cl.rednorte.ms_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.rednorte.ms_portal.dto.CentroMetricaDTO;
import cl.rednorte.ms_portal.dto.DashboardResumenDTO;
import cl.rednorte.ms_portal.dto.DashboardSecretariaDTO;
import cl.rednorte.ms_portal.service.MetricasService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/portal/metricas")
public class MetricasController {
    @Autowired
    private MetricasService service;

    @GetMapping("/resumen")
    public ResponseEntity<DashboardResumenDTO> getResumen() {
        return ResponseEntity.ok(service.obtenerResumenGlobal());
    }

    @GetMapping("/centros")
    public ResponseEntity<List<CentroMetricaDTO>> getMetricasCentros() {
        return ResponseEntity.ok(service.obtenerMetricasPorCentro());
    }

    @GetMapping("/centro/{centroId}/dashboard-secretaria")
    public ResponseEntity<DashboardSecretariaDTO> getDashboardSecretaria(
            @PathVariable Long centroId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate fechaAUsar = (fecha != null) ? fecha : LocalDate.now();
        return ResponseEntity.ok(service.obtenerDashboardSecretaria(centroId, fechaAUsar));
    }
}
