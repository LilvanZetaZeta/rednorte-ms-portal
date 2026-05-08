package cl.rednorte.ms_portal.repository.readonly;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import cl.rednorte.ms_portal.entity.readonly.UsuarioView;

/**
 * Repositorio de SOLO LECTURA para la tabla "usuario".
 *
 * Mismas reglas de defensa que ReservaViewRepository:
 *  - Extiende Repository<T, ID> (no JpaRepository) para NO heredar métodos
 *    de escritura (save, delete, etc).
 *  - @Component obligatorio porque Spring no auto-detecta repositorios
 *    que extienden directamente Repository.
 *
 * Solo expone los métodos de lectura que ms-portal necesita para:
 *  - Listar staff (todos los no-pacientes) en DashboardAdmin.tsx
 *  - Contar usuarios por rol para el dashboard del director (KPIs)
 *  - Resolver perfiles individuales por id o id_auth
 *  - Buscar médicos filtrados por especialidad (consulta de visualización)
 */
@Component
public interface UsuarioViewRepository extends Repository<UsuarioView, Long> {

    /**
     * Lookup individual por ID interno (PK numérica).
     */
    Optional<UsuarioView> findById(Long id);

    /**
     * Lookup por id_auth (UUID emitido por Supabase Auth).
     * Usado cuando el frontend pasa session.user.id directamente.
     */
    Optional<UsuarioView> findByIdAuth(String idAuth);

    /**
     * Lookup por correo. Útil para búsquedas administrativas
     * desde el panel del director.
     */
    Optional<UsuarioView> findByCorreo(String correo);

    /**
     * Lista TODOS los usuarios sin filtro. Solo para casos analíticos.
     * En la mayoría de pantallas se prefiere findByRolNot() abajo.
     */
    List<UsuarioView> findAll();

    /**
     * Conteo total de usuarios (todos los roles incluidos).
     */
    long count();

    /**
     * Lista todo el personal del staff (todos menos los pacientes).
     * Reemplaza al GET /api/usuarios/staff de ms-gestion, usado por
     * DashboardAdmin.tsx para mostrar el listado de personal con sus
     * roles y centros asignados.
     *
     * Recibimos el rol como String para no acoplar la firma al enum
     * de ms-gestion (cada microservicio tiene su propia copia del enum).
     */
    @Query(
        value = "SELECT * FROM usuario WHERE CAST(rol AS text) <> :rol",
        nativeQuery = true
    )
    List<UsuarioView> findAllExceptRoleNative(@Param("rol") String rol);

    /**
     * Busca médicos por nombre de especialidad.
     * Reemplaza al findByRolAndEspecialidades_NombreIgnoreCase() de ms-gestion.
     *
     * Usamos JPQL aquí (no nativo) porque la traducción del JOIN con la
     * tabla puente usuario_especialidad la maneja Hibernate automáticamente
     * gracias al mapeo @ManyToMany de UsuarioView.
     *
     * El comparador con CAST a text es necesario por el enum nativo
     * de PostgreSQL (rol_usuario), igual que en otras queries.
     */
    @Query(
        value = "SELECT DISTINCT u.* FROM usuario u " +
                "JOIN usuario_especialidad ue ON ue.usuario_id = u.id " +
                "JOIN especialidad e ON e.id = ue.especialidad_id " +
                "WHERE CAST(u.rol AS text) = 'MEDICO' " +
                "AND LOWER(e.nombre) = LOWER(:especialidad)",
        nativeQuery = true
    )
    List<UsuarioView> findMedicosByEspecialidadNombre(@Param("especialidad") String especialidad);

    /**
     * Conteo de usuarios por rol. Reemplaza al countByRol() de ms-gestion.
     * Necesario para DashboardResumenDTO (totalPacientes, totalMedicos).
     *
     * Mismo patrón nativeQuery + CAST a text que ReservaViewRepository
     * para manejar el enum nativo rol_usuario de PostgreSQL.
     */
    @Query(
        value = "SELECT count(*) FROM usuario WHERE CAST(rol AS text) = :rol",
        nativeQuery = true
    )
    long countByRolNative(@Param("rol") String rol);
}