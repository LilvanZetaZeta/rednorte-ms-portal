package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.rednorte.ms_portal.dto.PerfilPacienteRequest;
import cl.rednorte.ms_portal.entity.PerfilPaciente;
import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import cl.rednorte.ms_portal.repository.PerfilPacienteRepository;
import cl.rednorte.ms_portal.repository.readonly.UsuarioViewRepository;

@Service
public class PerfilPacienteService {

    @Autowired
    private PerfilPacienteRepository perfilPacienteRepository;
    
    @Autowired
    private UsuarioViewRepository usuarioViewRepository;

    // Agregar método:
    public PerfilPaciente obtenerPorIdAuth(String idAuth) {
        UsuarioView usuario = usuarioViewRepository.findByIdAuth(idAuth)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + idAuth));

        return perfilPacienteRepository.findByPacienteId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado para: " + idAuth));
    }
    

    public PerfilPaciente crear(PerfilPacienteRequest req) {
        if (perfilPacienteRepository.existsByPacienteId(req.getPacienteId())) {
            throw new RuntimeException("El paciente ya tiene un perfil registrado en el portal.");
        }
        PerfilPaciente perfil = new PerfilPaciente();
        perfil.setPacienteId(req.getPacienteId());
        perfil.setPrevision(req.getPrevision());
        perfil.setTelefonoContacto(req.getTelefonoContacto());
        return perfilPacienteRepository.save(perfil);
    }

    public PerfilPaciente obtenerPorId(Long id) {
        return perfilPacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
    }

    public PerfilPaciente obtenerPorPacienteId(Long pacienteId) {
        return perfilPacienteRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado para el paciente: " + pacienteId));
    }
    

    public PerfilPaciente actualizar(Long id, PerfilPacienteRequest req) {
        return perfilPacienteRepository.findById(id).map(perfil -> {
            if (req.getPrevision() != null)
                perfil.setPrevision(req.getPrevision());
            if (req.getTelefonoContacto() != null)
                perfil.setTelefonoContacto(req.getTelefonoContacto());
            return perfilPacienteRepository.save(perfil);
        }).orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
    }

    public void eliminar(Long id) {
        perfilPacienteRepository.deleteById(id);
    }
}


