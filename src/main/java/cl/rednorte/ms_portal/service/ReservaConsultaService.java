package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.repository.readonly.ReservaViewRepository;
import java.util.List;

@Service
public class ReservaConsultaService {
    @Autowired private ReservaViewRepository repository;

    public List<ReservaView> listarTodas() { return repository.findAll(); }
    public ReservaView obtenerPorId(Long id) { return repository.findById(id).orElseThrow(() -> new RuntimeException("Reserva no encontrada")); }
    public List<ReservaView> porPacienteIdAuth(String idAuth) { return repository.findByPaciente_IdAuth(idAuth); }
    public List<ReservaView> porCentro(Long centroId) { return repository.findByCentroId(centroId); }
    public List<ReservaView> porMedico(String idAuth) { return repository.findByMedicoId(repository.findByPaciente_IdAuth(idAuth).get(0).getMedico().getId()); } 
}