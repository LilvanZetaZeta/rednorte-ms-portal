package cl.rednorte.ms_portal.service;

import cl.rednorte.ms_portal.dto.HistorialCitaRequest;
import cl.rednorte.ms_portal.entity.HistorialCita;
import cl.rednorte.ms_portal.entity.HistorialCita.EstadoHistorico;
import cl.rednorte.ms_portal.repository.HistorialCitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialCitaService {

    @Autowired
    private HistorialCitaRepository historialCitaRepository;

    public HistorialCita crear(HistorialCitaRequest req) {
        HistorialCita cita = new HistorialCita();
        cita.setPacienteId(req.getPacienteId());
        cita.setDetalleProcedimiento(req.getDetalleProcedimiento());
        cita.setFechaCita(req.getFechaCita());
        cita.setEstadoHistorico(req.getEstadoHistorico());
        return historialCitaRepository.save(cita);
    }

    public HistorialCita obtenerPorId(Long id) {
        return historialCitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita en historial no encontrada"));
    }

    // RF.2 Historial: registro de citas finalizadas y canceladas del paciente
    public List<HistorialCita> obtenerHistorialPaciente(Long pacienteId) {
        return historialCitaRepository.findByPacienteIdOrderByFechaCitaDesc(pacienteId);
    }

    public List<HistorialCita> obtenerPorEstado(Long pacienteId, EstadoHistorico estado) {
        return historialCitaRepository.findByPacienteIdAndEstadoHistorico(pacienteId, estado);
    }

    public List<HistorialCita> listarTodos() {
        return historialCitaRepository.findAll();
    }
}