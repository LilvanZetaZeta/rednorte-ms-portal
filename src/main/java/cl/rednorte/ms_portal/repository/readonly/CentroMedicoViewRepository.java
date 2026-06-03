package cl.rednorte.ms_portal.repository.readonly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.rednorte.ms_portal.entity.readonly.CentroMedicoView;
import java.util.List;

@Repository
public interface CentroMedicoViewRepository extends JpaRepository<CentroMedicoView, Long> {
    List<CentroMedicoView> findByRegionAndComunaIgnoreCase(String region, String comuna);
    List<CentroMedicoView> findByComunaIgnoreCase(String comuna);
}