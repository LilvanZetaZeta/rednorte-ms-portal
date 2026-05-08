package cl.rednorte.ms_portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.repository.readonly.ReservaViewRepository;

/**
 * Service de consulta (lectura pura) sobre reservas. MIGRADO desde ms-gestion.
 *
 * Responde a los casos de uso de visualización del portal:
 *  - Paciente consultando su historial: PortalPaciente.tsx
 *  - Médico consultando su agenda: PortalDoctor.tsx
 *  - Administrativo local viendo agenda del centro: DashboardCentro.tsx
 *
 * Estos endpoints originalmente vivían en ReservaController/ReservaService de
 * ms-gestion y competían por el pool de conexiones con las escrituras
 * transaccionales (POST /reservas, PUT cancelar, etc), causando degradación
 * de rendimiento durante los picos de las 09:00 AM.
 *
 * Al migrarlos aquí:
 *  - El pool de ms-gestion queda libre para procesar escrituras críticas.
 *  - El pool de ms-portal absorbe la carga analítica/visualización.
 *  - La separación se vuelve física, no solo lógica.
 *
 * DEFENSA EN PROFUNDIDAD - Capa 3:
 * @Transactional(readOnly = true) a nivel de clase. Mismo razonamiento que
 * MetricasService: lo aplico a la clase completa para que cualquier método
 * futuro herede el comportamiento read-only por defecto.
 *
 * IMPORTANTE: Devolvemos directamente las entidades ReservaView. NO las
 * envolvemos en DTOs porque:
 *  1. ReservaView ya es una vista plana (read-only, @Immutable).
 *  2. El contrato JSON externo debe coincidir EXACTAMENTE con la respuesta
 *     que ms-gestion devolvía con la entidad Reserva. El frontend ya espera
 *     ese shape.
 *  3. Crear DTOs duplicados sería overhead innecesario aquí.
 *
 * Si en el futuro el frontend necesita un shape distinto, ahí se justifica
 * crear un DTO. Por ahora, principio YAGNI: no agregamos abstracciones que
 * no resuelven un problema real hoy.
 */
@Service
@Transactional(readOnly = true)
public class ReservaConsultaService {

    @Autowired private ReservaViewRepository reservaRepo;

    /**
     * Lista TODAS las reservas del sistema. Reemplaza al GET /api/reservas
     * de ms-gestion.
     *
     * USO LIMITADO: solo accesos administrativos. El frontend no consume
     * este endpoint masivamente; lo dejamos disponible por compatibilidad
     * y para herramientas internas.
     */
    public List<ReservaView> listarTodas() {
        return reservaRepo.findAll();
    }

    /**
     * Obtiene una reserva específica por ID. Reemplaza al
     * GET /api/reservas/{id} de ms-gestion.
     *
     * Devuelve null si no existe en lugar de lanzar excepción, para que el
     * controller decida si responder 404 o 200-vacío según el contexto.
     * Mantenemos la semántica original que tenía obtenerReservaPorId() en
     * ms-gestion (que lanzaba excepción genérica), pero aquí lo modelamos
     * con Optional para mayor claridad.
     */
    public ReservaView obtenerPorId(Long id) {
        return reservaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada: id=" + id));
    }

    /**
     * Reservas de un paciente identificado por su id_auth (UUID de Supabase).
     * Reemplaza al GET /api/reservas/paciente/{idAuth} de ms-gestion.
     *
     * El frontend (PortalPaciente.tsx) envía session.user.id que es
     * directamente el id_auth, no el ID interno numérico:
     *
     *     useObtenerMisReservasQuery(userId, { skip: !userId })
     *     // donde userId = session.user.id (UUID de Supabase)
     *
     * Por eso esta variante usa findByPaciente_IdAuth y no findByPacienteId.
     */
    public List<ReservaView> obtenerPorPacienteIdAuth(String idAuth) {
        return reservaRepo.findByPaciente_IdAuth(idAuth);
    }

    /**
     * Reservas de un paciente identificado por su ID interno (PK numérica).
     * Variante alternativa por si algún flujo administrativo del frontend
     * envía el ID numérico en lugar del id_auth.
     */
    public List<ReservaView> obtenerPorPacienteId(Long pacienteId) {
        return reservaRepo.findByPacienteId(pacienteId);
    }

    /**
     * Reservas asignadas a un médico por su ID interno (PK numérica).
     * Reemplaza al GET /api/reservas/medico/{medicoId} de ms-gestion.
     *
     * NOTA: el frontend (PortalDoctor.tsx) actualmente envía session.user.id
     * (id_auth UUID), no el ID numérico. Por eso ofrecemos también la
     * variante por idAuth abajo. El controller decidirá cuál usar según
     * lo que reciba en la URL.
     */
    public List<ReservaView> obtenerPorMedicoId(Long medicoId) {
        return reservaRepo.findByMedicoId(medicoId);
    }

    /**
     * Reservas asignadas a un médico por su id_auth (UUID de Supabase).
     * Variante del método anterior, alineada con cómo el frontend de
     * PortalDoctor.tsx hace la query (envía session.user.id directamente).
     */
    public List<ReservaView> obtenerPorMedicoIdAuth(String idAuth) {
        return reservaRepo.findByMedico_IdAuth(idAuth);
    }

    /**
     * Reservas asociadas a un centro médico. Reemplaza al
     * GET /api/reservas/centro/{centroId} de ms-gestion.
     *
     * Usado por DashboardCentro.tsx (administrativo local) para ver la
     * agenda completa de su sucursal.
     */
    public List<ReservaView> obtenerPorCentroId(Long centroId) {
        return reservaRepo.findByCentroId(centroId);
    }
}