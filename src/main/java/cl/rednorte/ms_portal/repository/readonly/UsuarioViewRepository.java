package cl.rednorte.ms_portal.repository.readonly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioViewRepository extends JpaRepository<UsuarioView, Long> {
    Optional<UsuarioView> findByCorreo(String correo);
    List<UsuarioView> findByRolNot(UsuarioView.RolUsuario rol);
    Optional<UsuarioView> findByRut(String rut);
    Optional<UsuarioView> findByIdAuth(String idAuth);
    List<UsuarioView> findByRolAndEspecialidades_NombreIgnoreCase(UsuarioView.RolUsuario rol, String especialidad);
    List<UsuarioView> findByRolAndCentroMedicoIsNull(UsuarioView.RolUsuario rol);
    long countByRol(UsuarioView.RolUsuario rol);

    @Query(value = "SELECT * FROM usuario WHERE rol != :rol", nativeQuery = true)
    List<UsuarioView> findAllExceptRoleNative(@Param("rol") String rol);

    @Query("SELECT u FROM UsuarioView u JOIN u.especialidades e WHERE u.rol = cl.rednorte.ms_portal.entity.readonly.UsuarioView.RolUsuario.MEDICO AND LOWER(e.nombre) = LOWER(:especialidad)")
    List<UsuarioView> findMedicosByEspecialidadNombre(@Param("especialidad") String especialidad);
}