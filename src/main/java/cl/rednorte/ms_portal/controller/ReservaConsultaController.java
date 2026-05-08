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

/**
 * Controller de consultas (lectura pura) sobre reservas. MIGRADO desde ms-gestion.
 *
 * IMPORTANTE - Decisión de routing (igual que MetricasController):
 *
 * El path elegido es /api/reservas/** y NO /api/portal/reservas/**.
 *
 * Mismo razonamiento que en MetricasController: el frontend (reservasApi.ts)
 * llama a estos paths exactos:
 *
 *     query: (idAuth) => `/reservas/paciente/${idAuth}`
 *     query: (centroId) => `/reservas/centro/${centroId}`
 *     query: (idAuth) => `/reservas/medico/${idAuth}`
 *
 * Si cambio los paths, tengo que tocar el frontend. Si los mantengo, solo
 * tengo que ajustar el API Gateway para que rutee SELECTIVAMENTE:
 *
 *   - GET /api/reservas/paciente/**     →  ms-portal (visualización)
 *   - GET /api/reservas/medico/**       →  ms-portal (visualización)
 *   - GET /api/reservas/centro/**       →  ms-portal (visualización)
 *   - GET /api/reservas/{id}            →  ms-portal (visualización)
 *   - GET /api/reservas                 →  ms-portal (visualización)
 *
 *   - POST   /api/reservas              →  ms-gestion (escritura - SE QUEDA)
 *   - PUT    /api/reservas/{id}         →  ms-gestion (escritura - SE QUEDA)
 *   - PUT    /api/reservas/{id}/cancelar→  ms-gestion (escritura - SE QUEDA)
 *   - PATCH  /api/reservas/{id}         →  ms-gestion (escritura - SE QUEDA)
 *   - DELETE /api/reservas/{id}         →  ms-gestion (escritura - SE QUEDA)
 *
 * Esto es POSIBLE en Spring Cloud Gateway porque permite predicates por
 * método HTTP (Path + Method). Lo configuraremos al final cuando hayamos
 * terminado los 14 archivos.
 *
 *
 * RESPONSABILIDADES DE ESTE CONTROLLER:
 *
 *  1. Reemplazar todos los GET de ReservaController de ms-gestion.
 *  2. Mantener el mismo contrato JSON (firmas y formato de respuesta).
 *  3. Actuar como punto de lectura puro: ningún método aquí puede
 *     causar mutación en la BD, ni siquiera por accidente, porque la
 *     cadena entera (controller -> service @readOnly -> repo Repository<>
 *     -> entity @Immutable) lo impide.
 *
 * ENDPOINTS PROPUESTOS:
 *
 *   GET /api/reservas                                  → listar todas
 *   GET /api/reservas/{id}                             → obtener por id
 *   GET /api/reservas/paciente/{idAuth}                → reservas del paciente (UUID Supabase)
 *   GET /api/reservas/paciente/numerico/{pacienteId}   → reservas del paciente (PK numérica)
 *   GET /api/reservas/medico/{idAuth}                  → agenda del médico (UUID Supabase)
 *   GET /api/reservas/medico/numerico/{medicoId}       → agenda del médico (PK numérica)
 *   GET /api/reservas/centro/{centroId}                → agenda del centro
 */
@RestController
@RequestMapping("/api/reservas")
public class ReservaConsultaController {

    @Autowired
    private ReservaConsultaService reservaConsultaService;

    /**
     * GET /api/reservas
     *
     * Lista TODAS las reservas del sistema. Reemplaza al GET listar() del
     * ReservaController de ms-gestion.
     *
     * USO LIMITADO: el frontend actual no consume este endpoint. Lo dejamos
     * disponible por compatibilidad con herramientas administrativas y para
     * no romper consumidores externos hipotéticos.
     */
    @GetMapping
    public ResponseEntity<List<ReservaView>> listar() {
        return ResponseEntity.ok(reservaConsultaService.listarTodas());
    }

    /**
     * GET /api/reservas/{id}
     *
     * Obtiene una reserva específica por ID. Reemplaza al GET /api/reservas/{id}
     * de ms-gestion.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservaView> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorId(id));
    }

    /**
     * GET /api/reservas/paciente/{idAuth}
     *
     * Reservas del paciente identificado por su id_auth (UUID de Supabase).
     * Reemplaza al endpoint /api/reservas/paciente/{idAuth} de ms-gestion
     * usado por PortalPaciente.tsx vía:
     *
     *     useObtenerMisReservasQuery(userId)  // userId = session.user.id (UUID)
     *
     * El path variable usa nombre genérico "idAuth" para dejar explícito
     * que es el UUID de Supabase, no el ID interno numérico.
     */
    @GetMapping("/paciente/{idAuth}")
    public ResponseEntity<List<ReservaView>> porPacienteIdAuth(@PathVariable String idAuth) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorPacienteIdAuth(idAuth));
    }

    /**
     * GET /api/reservas/paciente/numerico/{pacienteId}
     *
     * Reservas del paciente identificado por su ID interno numérico.
     * Variante alternativa por si algún flujo administrativo necesita
     * consultar por ID numérico en vez de UUID.
     *
     * El sub-path "/numerico/" evita la ambigüedad con /paciente/{idAuth}:
     * Spring no podría distinguir un Long de un String en la misma posición.
     */
    @GetMapping("/paciente/numerico/{pacienteId}")
    public ResponseEntity<List<ReservaView>> porPacienteId(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorPacienteId(pacienteId));
    }

    /**
     * GET /api/reservas/medico/{idAuth}
     *
     * Agenda del médico identificado por su id_auth (UUID de Supabase).
     * Reemplaza al endpoint /api/reservas/medico/{idAuth} de ms-gestion
     * usado por PortalDoctor.tsx vía:
     *
     *     useObtenerReservasPorMedicoQuery(idAuth)  // idAuth = session.user.id
     *
     * NOTA IMPORTANTE: El controller ORIGINAL en ms-gestion declara:
     *
     *     @GetMapping("/medico/{medicoId}")
     *     public ResponseEntity<List<Reserva>> porMedico(@PathVariable Long medicoId)
     *
     * Es decir, lo declara como Long. PERO en la práctica el frontend le
     * envía el UUID, lo que en ms-gestion causaría una excepción de
     * conversión de path variable. Esto sugiere que ese endpoint en ms-gestion
     * ya estaba con un bug latente o no se estaba usando correctamente.
     *
     * Aquí lo corregimos para que reciba String (UUID), que es lo que el
     * frontend realmente envía. Es uno de esos casos donde la migración
     * incidentalmente arregla un bug preexistente.
     */
    @GetMapping("/medico/{idAuth}")
    public ResponseEntity<List<ReservaView>> porMedicoIdAuth(@PathVariable String idAuth) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorMedicoIdAuth(idAuth));
    }

    /**
     * GET /api/reservas/medico/numerico/{medicoId}
     *
     * Agenda del médico identificado por su ID interno numérico.
     * Variante alternativa, simétrica con la del paciente.
     */
    @GetMapping("/medico/numerico/{medicoId}")
    public ResponseEntity<List<ReservaView>> porMedicoId(@PathVariable Long medicoId) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorMedicoId(medicoId));
    }

    /**
     * GET /api/reservas/centro/{centroId}
     *
     * Reservas asociadas a un centro médico. Reemplaza al endpoint
     * /api/reservas/centro/{centroId} de ms-gestion usado por
     * DashboardCentro.tsx (administrativo local) para ver la agenda
     * completa de su sucursal.
     *
     * Aquí no hay variante por idAuth porque los centros médicos no
     * tienen UUID externo: solo PK numérica.
     */
    @GetMapping("/centro/{centroId}")
    public ResponseEntity<List<ReservaView>> porCentro(@PathVariable Long centroId) {
        return ResponseEntity.ok(reservaConsultaService.obtenerPorCentroId(centroId));
    }
}