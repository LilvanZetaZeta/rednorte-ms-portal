package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import cl.rednorte.ms_portal.repository.readonly.UsuarioViewRepository;
import java.util.List;

@Service
public class UsuarioConsultaService {
    @Autowired private UsuarioViewRepository repository;

    public List<UsuarioView> listarTodos() { return repository.findAll(); }
    public UsuarioView obtenerPorId(Long id) { return repository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado")); }
    public UsuarioView obtenerPorIdAuth(String idAuth) { return repository.findByIdAuth(idAuth).orElseThrow(() -> new RuntimeException("Usuario no encontrado")); }
    public List<UsuarioView> buscarMedicosPorEspecialidad(String especialidad) { return repository.findByRolAndEspecialidades_NombreIgnoreCase(UsuarioView.RolUsuario.MEDICO, especialidad); }
    public List<UsuarioView> listarPersonalStaff() { return repository.findByRolNot(UsuarioView.RolUsuario.PACIENTE); }
    public List<UsuarioView> listarAdministradoresDisponibles() { return repository.findByRolAndCentroMedicoIsNull(UsuarioView.RolUsuario.ADMINISTRATIVO); }
}