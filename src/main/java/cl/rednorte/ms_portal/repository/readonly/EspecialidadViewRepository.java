package cl.rednorte.ms_portal.repository.readonly;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import cl.rednorte.ms_portal.entity.readonly.EspecialidadView;

/**
 * Repositorio de SOLO LECTURA para la tabla "especialidad".
 *
 * Mismas reglas de defensa que los repositorios anteriores:
 *  - Extiende Repository<T, ID> (no JpaRepository) para NO heredar métodos
 *    de escritura.
 *  - @Component obligatorio porque Spring no auto-detecta repositorios
 *    que extienden directamente Repository.
 *
 * Solo expone los métodos de lectura que ms-portal necesita para:
 *  - Contar especialidades para el KPI totalEspecialidades del DashboardResumenDTO.
 *
 * El listado completo de especialidades para el flujo de reserva
 * (GET /api/especialidades) se queda en ms-gestion porque es parte
 * del flujo transaccional de creación de citas, igual que los centros médicos.
 *
 * El ABM de especialidades (crear, renombrar, eliminar) también permanece
 * en ms-gestion vía EspecialidadController y EspecialidadService.
 */
@Component
public interface EspecialidadViewRepository extends Repository<EspecialidadView, Long> {

    /**
     * Lookup individual por ID. Útil para resolver nombres de especialidad
     * en respuestas analíticas si llegaran a necesitarse.
     */
    Optional<EspecialidadView> findById(Long id);

    /**
     * Lista todas las especialidades. Disponible para casos analíticos.
     */
    List<EspecialidadView> findAll();

    /**
     * Conteo total. Necesario para DashboardResumenDTO.totalEspecialidades.
     */
    long count();
}