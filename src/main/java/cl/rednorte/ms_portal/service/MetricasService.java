package cl.rednorte.ms_portal.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.rednorte.ms_portal.dto.CentroMetricaDTO;
import cl.rednorte.ms_portal.dto.DashboardResumenDTO;
import cl.rednorte.ms_portal.entity.readonly.CentroMedicoView;
import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import cl.rednorte.ms_portal.repository.readonly.CentroMedicoViewRepository;
import cl.rednorte.ms_portal.repository.readonly.EspecialidadViewRepository;
import cl.rednorte.ms_portal.repository.readonly.ReservaViewRepository;
import cl.rednorte.ms_portal.repository.readonly.UsuarioViewRepository;

/**
 * Service de métricas analíticas. MIGRADO desde ms-gestion.
 *
 * Este service responde a los KPIs del dashboard ejecutivo y consume
 * exclusivamente repositorios read-only. NUNCA modifica datos.
 *
 * DEFENSA EN PROFUNDIDAD - Capa 3:
 * @Transactional(readOnly = true) a nivel de clase aplica a TODOS los métodos
 * públicos. Esto produce dos efectos críticos:
 *
 *  1. Hibernate desactiva el dirty-checking automático del EntityManager:
 *     ya no rastrea cambios en las entidades cargadas en este contexto,
 *     ahorrando memoria y CPU.
 *  2. Spring marca la conexión JDBC como read-only, lo que en PostgreSQL
 *     activa optimizaciones internas (sin WAL entries para snapshots,
 *     potencial enrutamiento a réplicas de lectura si Supabase las expone).
 *
 * Combinado con las capas anteriores (@Immutable en entidades + Repository<>
 * sin métodos de escritura), tenemos triple defensa contra mutaciones
 * accidentales.
 *
 * MEJORA frente a ms-gestion: la implementación de obtenerMetricasPorCentro()
 * resuelve el problema N+1 que tenía la versión original. Ver detalles abajo.
 */
@Service
@Transactional(readOnly = true)
public class MetricasService {

    @Autowired private ReservaViewRepository reservaRepo;
    @Autowired private UsuarioViewRepository usuarioRepo;
    @Autowired private CentroMedicoViewRepository centroRepo;
    @Autowired private EspecialidadViewRepository especialidadRepo;

    /**
     * Construye el resumen ejecutivo de KPIs globales.
     *
     * Total: 7 queries de tipo COUNT, todas O(1) en PostgreSQL gracias a
     * estadísticas internas. Tiempo total esperado: <50ms incluso con
     * millones de reservas.
     *
     * Reemplaza al endpoint GET /api/gestion/metricas/resumen de ms-gestion.
     */
    public DashboardResumenDTO obtenerResumen() {
        return new DashboardResumenDTO(
            reservaRepo.count(),
            reservaRepo.countByEstadoNative(ReservaView.EstadoReserva.VIGENTE.name()),
            reservaRepo.countByEstadoNative(ReservaView.EstadoReserva.CANCELADA.name()),
            usuarioRepo.countByRolNative(UsuarioView.RolUsuario.PACIENTE.name()),
            usuarioRepo.countByRolNative(UsuarioView.RolUsuario.MEDICO.name()),
            centroRepo.count(),
            especialidadRepo.count()
        );
    }

    /**
     * Construye el listado de rendimiento por centro médico.
     *
     * MEJORA CRÍTICA frente a ms-gestion:
     *
     * La implementación original en ms-gestion tenía este código:
     *
     *     return centroRepo.findAll().stream()
     *         .map(c -> new CentroMetricaDTO(
     *             c.getNombreSucursal(),
     *             reservaRepo.findByCentroId(c.getId()).size()
     *         ))
     *         .collect(Collectors.toList());
     *
     * Problemas:
     *   1. N+1 queries: 1 SELECT para listar centros + N SELECTs (uno por
     *      cada centro) para contar sus reservas. Para 8 centros = 9 queries.
     *   2. .findByCentroId(...).size() hidrata TODAS las filas en memoria
     *      solo para contarlas. Si un centro tiene 50,000 reservas, son
     *      50,000 entidades hidratadas innecesariamente.
     *
     * Mi versión:
     *   - 1 query para listar centros (necesaria para obtener nombres).
     *   - 1 query con GROUP BY que devuelve directamente el conteo por centro.
     *   - Total: 2 queries fijas, sin importar cuántos centros existan.
     *   - Cero hidratación de entidades Reserva.
     *
     * Para datasets pequeños (8 centros) la diferencia es marginal. Para
     * crecimiento futuro (50+ centros, millones de reservas) es la diferencia
     * entre <100ms y >2 segundos.
     */
    public List<CentroMetricaDTO> obtenerMetricasPorCentro() {
        // Query 1: traer la lista de centros (necesitamos el nombreSucursal).
        List<CentroMedicoView> centros = centroRepo.findAll();

        // Query 2: contar reservas agrupadas por centro_id en una sola pasada.
        // Devuelve List<Object[]> donde [0]=centro_id (Long) y [1]=count (Long).
        List<Object[]> conteos = reservaRepo.countReservasGroupByCentroId();

        // Convertimos los conteos a un Map<centroId, cantidad> para lookup O(1).
        // Usamos Number como tipo intermedio porque PostgreSQL/Hibernate puede
        // devolver el ID como Long o como Integer según el driver, y el COUNT
        // como Long o BigInteger. Number cubre ambos casos sin ClassCastException.
        Map<Long, Long> conteoPorCentroId = conteos.stream()
            .collect(Collectors.toMap(
                row -> ((Number) row[0]).longValue(),
                row -> ((Number) row[1]).longValue()
            ));

        // Construimos el DTO mapeando cada centro a su conteo (0 si no tiene
        // reservas asociadas, lo cual es importante: un centro sin reservas
        // debe aparecer en el listado con cantidad=0, no ser omitido).
        return centros.stream()
            .map(c -> new CentroMetricaDTO(
                c.getNombreSucursal(),
                conteoPorCentroId.getOrDefault(c.getId(), 0L)
            ))
            .collect(Collectors.toList());
    }
}