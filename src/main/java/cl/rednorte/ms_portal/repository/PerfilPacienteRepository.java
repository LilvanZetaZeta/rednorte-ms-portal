package cl.rednorte.ms_portal.repository;

import cl.rednorte.ms_portal.entity.PerfilPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilPacienteRepository extends JpaRepository<PerfilPaciente, Long> {
    Optional<PerfilPaciente> findByPacienteId(Long pacienteId);
    boolean existsByPacienteId(Long pacienteId);
}