package cl.rednorte.ms_portal.repository.readonly;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import cl.rednorte.ms_portal.entity.readonly.ReservaView;

@Component
public interface ReservaViewRepository extends Repository<ReservaView, Long> {

    Optional<ReservaView> findById(Long id);

    List<ReservaView> findAll();

    long count();

    List<ReservaView> findByPacienteId(Long pacienteId);

    List<ReservaView> findByPaciente_IdAuth(String idAuth);

    List<ReservaView> findByMedicoId(Long medicoId);

    List<ReservaView> findByMedico_IdAuth(String idAuth);

    List<ReservaView> findByCentroId(Long centroId);

    @Query(value = "SELECT count(*) FROM reserva WHERE CAST(estado AS text) = :estado", nativeQuery = true)
    long countByEstadoNative(@Param("estado") String estado);

    @Query(value = "SELECT centro_id, COUNT(*) FROM reserva GROUP BY centro_id", nativeQuery = true)
    List<Object[]> countReservasGroupByCentroId();
}