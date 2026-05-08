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

/**
 * Controller de métricas analíticas. MIGRADO desde ms-gestion.
 *
 * IMPORTANTE - Decisión de routing:
 * El path elegido es /api/gestion/metricas/** y NO /api/portal/metricas/**.
 *
 * ¿Por qué mantenemos el prefijo "/api/gestion/" si esto ahora vive en
 * ms-portal? Porque el API Gateway (api-gateway/src/main/resources/application.yml)
 * tiene esta regla de enrutamiento:
 *
 *     - id: ms-gestion-route
 *       uri: ${MS_GESTION_URL:http://localhost:8081}
 *       predicates:
 *         - Path=/api/gestion/**,/api/centros-medicos/**,/api/usuarios/**,
 *                /api/reservas/**,/api/lista-espera/**
 *
 *     - id: ms-portal-route
 *       uri: ${MS_PORTAL_URL:http://localhost:8082}
 *       predicates:
 *         - Path=/api/portal/**,/api/perfil-pacientes/**,/api/historial-citas/**
 *
 * Y el frontend (services/metricasApi.ts) llama exactamente a:
 *
 *     query: () => '/gestion/metricas/resumen'
 *     query: () => '/gestion/metricas/centros'
 *
 * Tenemos dos opciones:
 *
 *   OPCIÓN A: Mover el path acá a /api/portal/metricas y modificar el
 *             frontend (metricasApi.ts) para que apunte al nuevo path.
 *
 *   OPCIÓN B: Mantener /api/gestion/metricas acá, y modificar SOLO el
 *             API Gateway para que las rutas /api/gestion/metricas/**
 *             apunten a ms-portal en lugar de ms-gestion.
 *
 * Elijo OPCIÓN B por las siguientes razones:
 *
 *   1. ZERO cambios en el frontend. La migración es invisible para React.
 *   2. El cambio en el Gateway es de UNA sola línea (agregar una regla más
 *      específica que tenga prioridad sobre /api/gestion/**).
 *   3. Si rompemos algo durante la migración, el rollback es trivial: solo
 *      revertir la regla del Gateway. No hay que coordinar PRs en frontend
 *      ni redesplegar React.
 *   4. Da tiempo para hacer una migración progresiva: mañana, en un PR
 *      separado, podemos renombrar paths y avisar al frontend con calma.
 *
 * En ARCHIVOS POSTERIORES (no en este step), tendremos que actualizar el
 * application.yml del API Gateway para que rutee /api/gestion/metricas/**
 * hacia ms-portal. Por ahora, este controller solo expone el endpoint con
 * el path correcto: el routing del Gateway lo arreglamos al final.
 */
@RestController
@RequestMapping("/api/gestion/metricas")
public class MetricasController {

    @Autowired
    private MetricasService metricasService;

    /**
     * GET /api/gestion/metricas/resumen
     *
     * Devuelve los KPIs globales del sistema (totales y conteos por estado/rol).
     * Consumido por:
     *   - PortalDirector.tsx (panel ejecutivo del director)
     *   - DashboardCentro.tsx (administrativo local - usa solo algunos campos)
     *   - PortalSecretaria.tsx (KPIs superiores de la pantalla de operaciones)
     *
     * REEMPLAZA EXACTAMENTE al endpoint que vivía en
     *   cl.rednorte.ms_gestion.controller.MetricasController.getResumen()
     *
     * El JSON de respuesta es idéntico bit-a-bit. El frontend no nota la
     * migración salvo por un cambio en la latencia (debería ser MEJOR aquí
     * porque ms-portal no compite por el pool con escrituras transaccionales).
     */
    @GetMapping("/resumen")
    public ResponseEntity<DashboardResumenDTO> getResumen() {
        return ResponseEntity.ok(metricasService.obtenerResumen());
    }

    /**
     * GET /api/gestion/metricas/centros
     *
     * Devuelve el listado de rendimiento por sucursal (nombre + cantidad
     * de reservas). Consumido por PortalDirector.tsx para renderizar el
     * gráfico de barras "Rendimiento por Sucursal".
     *
     * REEMPLAZA EXACTAMENTE al endpoint que vivía en
     *   cl.rednorte.ms_gestion.controller.MetricasController.getPorCentro()
     *
     * Ganancia colateral: la implementación interna ahora usa GROUP BY en
     * lugar del antipatrón N+1 que tenía ms-gestion. Para 8 centros, pasamos
     * de 9 queries a 2.
     */
    @GetMapping("/centros")
    public ResponseEntity<List<CentroMetricaDTO>> getPorCentro() {
        return ResponseEntity.ok(metricasService.obtenerMetricasPorCentro());
    }
}