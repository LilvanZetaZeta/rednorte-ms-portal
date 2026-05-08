package cl.rednorte.ms_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.rednorte.ms_portal.dto.CentroMetricaDTO;
import cl.rednorte.ms_portal.dto.DashboardResumenDTO;
import cl.rednorte.ms_portal.service.MetricasService;

@RestController
@RequestMapping("/api/gestion/metricas")
public class MetricasController {

    @Autowired
    private MetricasService metricasService;

    @GetMapping("/resumen")
    public ResponseEntity<DashboardResumenDTO> getResumen() {
        return ResponseEntity.ok(metricasService.obtenerResumen());
    }

    @GetMapping("/centros")
    public ResponseEntity<List<CentroMetricaDTO>> getPorCentro() {
        return ResponseEntity.ok(metricasService.obtenerMetricasPorCentro());
    }
}