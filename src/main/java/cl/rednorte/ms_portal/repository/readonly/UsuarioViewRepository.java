package cl.rednorte.ms_portal.repository.readonly;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import cl.rednorte.ms_portal.entity.readonly.UsuarioView;

@Component
public interface UsuarioViewRepository extends Repository<UsuarioView, Long> {

    Optional<UsuarioView> findById(Long id);

    Optional<UsuarioView> findByIdAuth(String idAuth);

    Optional<UsuarioView> findByCorreo(String correo);

    List<UsuarioView> findAll();

    long count();

    @Query(value = "SELECT * FROM usuario WHERE CAST(rol AS text) <> :rol", nativeQuery = true)
    List<UsuarioView> findAllExceptRoleNative(@Param("rol") String rol);

    @Query(value = "SELECT DISTINCT u.* FROM usuario u " +
            "JOIN usuario_especialidad ue ON ue.usuario_id = u.id " +
            "JOIN especialidad e ON e.id = ue.especialidad_id " +
            "WHERE CAST(u.rol AS text) = 'MEDICO' " +
            "AND LOWER(e.nombre) = LOWER(:especialidad)", nativeQuery = true)
    List<UsuarioView> findMedicosByEspecialidadNombre(@Param("especialidad") String especialidad);

    
    @Query(value = "SELECT DISTINCT u.* FROM usuario u " +
            "JOIN usuario_especialidad ue ON ue.usuario_id = u.id " +
            "WHERE CAST(u.rol AS text) = 'MEDICO' " +
            "AND u.centro_medico_id = :centroId " +
            "AND ue.especialidad_id = :especialidadId", nativeQuery = true)
    List<UsuarioView> findMedicosByCentroAndEspecialidad(
            @Param("centroId") Long centroId, 
            @Param("especialidadId") Long especialidadId);

    @Query(value = "SELECT count(*) FROM usuario WHERE CAST(rol AS text) = :rol", nativeQuery = true)
    long countByRolNative(@Param("rol") String rol);
}