package cl.rednorte.ms_portal.repository;

import cl.rednorte.ms_portal.entity.HistorialCita;
import cl.rednorte.ms_portal.entity.HistorialCita.EstadoHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialCitaRepository extends JpaRepository<HistorialCita, Long> {
    List<HistorialCita> findByPacienteIdOrderByFechaCitaDesc(Long pacienteId);
    List<HistorialCita> findByPacienteIdAndEstadoHistorico(Long pacienteId, EstadoHistorico estado);
}