package cl.rednorte.ms_portal.repository.readonly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.dto.CentroMetricaDTO;
import java.util.List;

@Repository
public interface ReservaViewRepository extends JpaRepository<ReservaView, Long> {
    List<ReservaView> findByPaciente_IdAuth(String idAuth);
    List<ReservaView> findByCentroId(Long centroId);
    List<ReservaView> findByMedicoId(Long medicoId);
    long countByEstado(ReservaView.EstadoReserva estado);

    @Query("SELECT new cl.rednorte.ms_portal.dto.CentroMetricaDTO(c.nombreSucursal, COUNT(r)) " +
           "FROM CentroMedicoView c LEFT JOIN ReservaView r ON r.centro.id = c.id " +
           "GROUP BY c.nombreSucursal")
    List<CentroMetricaDTO> obtenerMetricasPorCentro();
}