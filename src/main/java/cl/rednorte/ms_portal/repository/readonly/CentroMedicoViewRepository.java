package cl.rednorte.ms_portal.repository.readonly;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import cl.rednorte.ms_portal.entity.readonly.CentroMedicoView;

@Component
public interface CentroMedicoViewRepository extends Repository<CentroMedicoView, Long> {

    Optional<CentroMedicoView> findById(Long id);

    List<CentroMedicoView> findAll();

    long count();
}