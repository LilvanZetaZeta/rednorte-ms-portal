package cl.rednorte.ms_portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.repository.readonly.ReservaViewRepository;

@Service
@Transactional(readOnly = true)
public class ReservaConsultaService {

    @Autowired
    private ReservaViewRepository reservaRepo;

    public List<ReservaView> listarTodas() {
        return reservaRepo.findAll();
    }

    public ReservaView obtenerPorId(Long id) {
        return reservaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada: id=" + id));
    }

    public List<ReservaView> obtenerPorPacienteIdAuth(String idAuth) {
        return reservaRepo.findByPaciente_IdAuth(idAuth);
    }

    public List<ReservaView> obtenerPorPacienteId(Long pacienteId) {
        return reservaRepo.findByPacienteId(pacienteId);
    }

    public List<ReservaView> obtenerPorMedicoId(Long medicoId) {
        return reservaRepo.findByMedicoId(medicoId);
    }

    public List<ReservaView> obtenerPorMedicoIdAuth(String idAuth) {
        return reservaRepo.findByMedico_IdAuth(idAuth);
    }

    public List<ReservaView> obtenerPorCentroId(Long centroId) {
        return reservaRepo.findByCentroId(centroId);
    }
}