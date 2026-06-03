package cl.rednorte.ms_portal.repository.readonly;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.dto.CentroMetricaDTO;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaViewRepository extends JpaRepository<ReservaView, Long> {
    List<ReservaView> findByPaciente_IdAuth(String idAuth);
    
    // ✅ PAGINACIÓN: Se agregó Pageable para evitar traer miles de registros
    Page<ReservaView> findByCentroId(Long centroId, Pageable pageable);
    Page<ReservaView> findByMedicoId(Long medicoId, Pageable pageable);
    long countByEstado(ReservaView.EstadoReserva estado);

    @Query("SELECT new cl.rednorte.ms_portal.dto.CentroMetricaDTO(c.nombreSucursal, COUNT(r)) " +
           "FROM CentroMedicoView c LEFT JOIN ReservaView r ON r.centro.id = c.id " +
           "GROUP BY c.nombreSucursal")
    List<CentroMetricaDTO> obtenerMetricasPorCentro();

    // Métricas para dashboard de secretaria - conteos por estado para una fecha específica
    @Query("""
        SELECT
            COUNT(r),
            SUM(CASE WHEN r.estado = cl.rednorte.ms_portal.entity.readonly.ReservaView.EstadoReserva.VIGENTE THEN 1 ELSE 0 END),
            SUM(CASE WHEN r.estado = cl.rednorte.ms_portal.entity.readonly.ReservaView.EstadoReserva.CONFIRMADA THEN 1 ELSE 0 END),
            SUM(CASE WHEN r.estado IN (cl.rednorte.ms_portal.entity.readonly.ReservaView.EstadoReserva.VIGENTE, cl.rednorte.ms_portal.entity.readonly.ReservaView.EstadoReserva.CONFIRMADA) THEN 1 ELSE 0 END),
            SUM(CASE WHEN r.estado = cl.rednorte.ms_portal.entity.readonly.ReservaView.EstadoReserva.CANCELADA THEN 1 ELSE 0 END)
        FROM ReservaView r
        WHERE r.centro.id = :centroId
          AND FUNCTION('date', r.fechaHora) = :fecha
    """)
    Object[] obtenerConteosDashboardSecretaria(
        @Param("centroId") Long centroId,
        @Param("fecha") LocalDate fecha
    );

    // Contar médicos por centro
    @Query("""
        SELECT COUNT(u)
        FROM UsuarioView u
        WHERE u.centroMedico.id = :centroId
          AND u.rol = 'MEDICO'
    """)
    Long countMedicosByCentroId(@Param("centroId") Long centroId);
}
