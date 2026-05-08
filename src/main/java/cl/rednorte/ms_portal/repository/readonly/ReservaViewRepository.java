package cl.rednorte.ms_portal.repository.readonly;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import cl.rednorte.ms_portal.entity.readonly.ReservaView;

/**
 * Repositorio de SOLO LECTURA para la tabla "reserva".
 *
 * DECISIÓN ARQUITECTÓNICA CRÍTICA:
 * Extiende org.springframework.data.repository.Repository (interfaz base vacía)
 * en lugar de JpaRepository o CrudRepository.
 *
 * ¿Por qué?
 * JpaRepository hereda métodos como save(), saveAll(), delete(), deleteById(),
 * deleteAll(), flush(), etc. Si extendiéramos JpaRepository, Spring Data
 * generaría automáticamente esas implementaciones y cualquier desarrollador
 * podría llamar reservaViewRepository.save(...) y persistir cambios en la
 * tabla "reserva", violando la frontera de dominio.
 *
 * Al extender solo Repository<T, ID>, Spring Data NO genera ningún método
 * mutante. Solo expone los métodos que declaramos explícitamente abajo,
 * todos de lectura. Es la barrera más fuerte a nivel de API del repositorio.
 *
 * Combinado con @Immutable en la entidad ReservaView, tenemos:
 *   - Capa 1: La entidad rechaza cualquier flush con cambios.
 *   - Capa 2: El repositorio no expone métodos para intentarlo.
 *
 * @Component es necesario aquí porque al no extender una interfaz que ya
 * esté anotada como @Repository, Spring no la detecta automáticamente.
 */
@Component
public interface ReservaViewRepository extends Repository<ReservaView, Long> {

    /**
     * Lookup individual por ID. Reemplaza al GET /api/reservas/{id} de ms-gestion.
     */
    Optional<ReservaView> findById(Long id);

    /**
     * Lista TODAS las reservas. Reemplaza al GET /api/reservas listar() de ms-gestion.
     * Solo se usa para casos administrativos/analíticos.
     */
    List<ReservaView> findAll();

    /**
     * Conteo total de reservas. Necesario para DashboardResumenDTO.totalReservas.
     */
    long count();

    /**
     * Reservas de un paciente por su ID interno (PK numérica).
     * Reemplaza al endpoint GET /api/reservas/paciente/{pacienteId} cuando
     * el cliente ya tiene el ID numérico.
     */
    List<ReservaView> findByPacienteId(Long pacienteId);

    /**
     * Reservas de un paciente por su id_auth (UUID de Supabase).
     * Reemplaza al endpoint GET /api/reservas/paciente/{idAuth} usado por
     * el frontend en PortalPaciente.tsx (recibe directamente session.user.id).
     */
    List<ReservaView> findByPaciente_IdAuth(String idAuth);

    /**
     * Reservas asignadas a un médico por su ID interno (PK numérica).
     * Reemplaza al endpoint GET /api/reservas/medico/{medicoId} usado por
     * PortalDoctor.tsx para mostrar la agenda del día.
     */
    List<ReservaView> findByMedicoId(Long medicoId);

    /**
     * Reservas asignadas a un médico por su id_auth (UUID de Supabase).
     * El frontend de PortalDoctor.tsx envía session.user.id (no el ID numérico),
     * así que necesitamos esta variante.
     */
    List<ReservaView> findByMedico_IdAuth(String idAuth);

    /**
     * Reservas de un centro médico. Reemplaza al endpoint GET /api/reservas/centro/{centroId}
     * usado por DashboardCentro.tsx (administrativo local).
     */
    List<ReservaView> findByCentroId(Long centroId);

    // ─────────────────────────────────────────────────────────────────
    //  Queries para MetricasService
    // ─────────────────────────────────────────────────────────────────

    /**
     * Conteo de reservas por estado. Reemplaza al countByEstado() del
     * ReservaRepository de ms-gestion.
     *
     * IMPORTANTE: Usamos nativeQuery=true con CAST(estado AS text) porque
     * la columna "estado" en PostgreSQL es de tipo enum nativo (estado_reserva)
     * y JPQL no puede comparar directamente con un enum de Java.
     * Mantenemos el mismo patrón que ya usa ms-gestion para coherencia.
     */
    @Query(
        value = "SELECT count(*) FROM reserva WHERE CAST(estado AS text) = :estado",
        nativeQuery = true
    )
    long countByEstadoNative(@Param("estado") String estado);

    /**
     * Conteo de reservas agrupadas por centro_id, en una sola query.
     *
     * ESTO ES UNA MEJORA CRÍTICA frente a ms-gestion. La implementación
     * actual de MetricasService.obtenerMetricasPorCentro() en ms-gestion
     * sufre de N+1 query: hace 1 SELECT para listar centros y luego
     * N SELECTs (uno por cada centro) para contar sus reservas.
     *
     * Aquí lo resolvemos en una ÚNICA query con GROUP BY. Para 8 centros
     * pasamos de 9 queries a 1.
     *
     * Devuelve Object[] donde:
     *   [0] = centro_id (Long)
     *   [1] = cantidad de reservas (Long)
     */
    @Query(
        value = "SELECT centro_id, COUNT(*) FROM reserva GROUP BY centro_id",
        nativeQuery = true
    )
    List<Object[]> countReservasGroupByCentroId();
}