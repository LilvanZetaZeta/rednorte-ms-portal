package cl.rednorte.ms_portal.repository.readonly;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import cl.rednorte.ms_portal.entity.readonly.EspecialidadView;

@Component
public interface EspecialidadViewRepository extends Repository<EspecialidadView, Long> {

    Optional<EspecialidadView> findById(Long id);

    List<EspecialidadView> findAll();

    long count();
}