package cl.rednorte.ms_portal.repository.readonly;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import cl.rednorte.ms_portal.entity.readonly.CentroMedicoView;

/**
 * Repositorio de SOLO LECTURA para la tabla "centro_medico".
 *
 * Mismas reglas de defensa que los repositorios anteriores:
 *  - Extiende Repository<T, ID> (no JpaRepository) para NO heredar métodos
 *    de escritura.
 *  - @Component obligatorio porque Spring no auto-detecta repositorios
 *    que extienden directamente Repository.
 *
 * Solo expone los métodos de lectura que ms-portal necesita para:
 *  - Listar centros en el dashboard del director (PortalDirector).
 *  - Resolver el nombre de un centro a partir de su ID (en historiales,
 *    métricas por centro, etc).
 *  - Contar centros para el KPI totalCentros del DashboardResumenDTO.
 *
 * El ABM completo de centros (crear, editar, eliminar) sigue siendo
 * responsabilidad exclusiva de ms-gestion vía CentroMedicoController
 * y CentroMedicoService. Búsquedas por región/comuna también permanecen
 * allá porque son parte del flujo de creación de reservas (transaccional).
 */
@Component
public interface CentroMedicoViewRepository extends Repository<CentroMedicoView, Long> {

    /**
     * Lookup individual por ID. Necesario para resolver nombres de centro
     * en respuestas analíticas o de historial.
     */
    Optional<CentroMedicoView> findById(Long id);

    /**
     * Lista TODOS los centros médicos. Necesario para construir el listado
     * de métricas por centro en MetricasService.
     */
    List<CentroMedicoView> findAll();

    /**
     * Conteo total de centros. Necesario para DashboardResumenDTO.totalCentros.
     */
    long count();
}