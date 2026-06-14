package cl.rednorte.ms_portal.repository.readonly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.rednorte.ms_portal.entity.readonly.PerfilPacienteView;
import java.util.Optional;

@Repository
public interface PerfilPacienteViewRepository extends JpaRepository<PerfilPacienteView, Long> {
    Optional<PerfilPacienteView> findByIdAuth(String idAuth);
}