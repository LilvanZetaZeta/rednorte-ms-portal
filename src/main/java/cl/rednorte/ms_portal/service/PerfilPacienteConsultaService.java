package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.rednorte.ms_portal.entity.readonly.PerfilPacienteView;
import cl.rednorte.ms_portal.repository.readonly.PerfilPacienteViewRepository;

@Service
public class PerfilPacienteConsultaService {
    @Autowired private PerfilPacienteViewRepository repository;

    public PerfilPacienteView porIdAuth(String idAuth) { return repository.findByIdAuth(idAuth).orElseThrow(() -> new RuntimeException("Perfil no encontrado")); }
}