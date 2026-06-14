package cl.rednorte.ms_portal.repository.readonly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.rednorte.ms_portal.entity.readonly.EspecialidadView;
import java.util.Optional;

@Repository
public interface EspecialidadViewRepository extends JpaRepository<EspecialidadView, Long> {
    Optional<EspecialidadView> findByNombreIgnoreCase(String nombre);
}