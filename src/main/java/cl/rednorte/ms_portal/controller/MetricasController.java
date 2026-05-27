package cl.rednorte.ms_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.rednorte.ms_portal.dto.CentroMetricaDTO;
import cl.rednorte.ms_portal.dto.DashboardResumenDTO;
import cl.rednorte.ms_portal.service.MetricasService;
import java.util.List;

@RestController
@RequestMapping("/api/portal/metricas")
public class MetricasController {
    @Autowired private MetricasService service;

    @GetMapping("/resumen")
    public ResponseEntity<DashboardResumenDTO> getResumen() { return ResponseEntity.ok(service.obtenerResumenGlobal()); }

    @GetMapping("/centros")
    public ResponseEntity<List<CentroMetricaDTO>> getMetricasCentros() { return ResponseEntity.ok(service.obtenerMetricasPorCentro()); }
}