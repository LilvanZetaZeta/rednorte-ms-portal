package cl.rednorte.ms_portal.repository.readonly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.rednorte.ms_portal.entity.readonly.HistorialCitaView;
import java.util.List;

@Repository
public interface HistorialCitaViewRepository extends JpaRepository<HistorialCitaView, Long> {
    List<HistorialCitaView> findByPacienteId(Long pacienteId);
}