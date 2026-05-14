package cl.rednorte.ms_portal.repository.readonly;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import cl.rednorte.ms_portal.entity.readonly.EspecialidadView;

@Component
public interface EspecialidadViewRepository extends Repository<EspecialidadView, Long> {

    Optional<EspecialidadView> findById(Long id);

    List<EspecialidadView> findAll();

    @Query("SELECT DISTINCT e FROM UsuarioView u JOIN u.especialidades e WHERE u.centroMedico.id = :centroId")
    List<EspecialidadView> findByCentroId(@Param("centroId") Long centroId);

    long count();
}