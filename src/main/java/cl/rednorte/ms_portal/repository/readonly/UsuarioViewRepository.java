package cl.rednorte.ms_portal.repository.readonly;

import org.springframework.data.jpa.repository.JpaRepository;
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
}