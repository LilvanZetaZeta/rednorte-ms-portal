package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.rednorte.ms_portal.entity.readonly.HistorialCitaView;
import cl.rednorte.ms_portal.repository.readonly.HistorialCitaViewRepository;
import java.util.List;

@Service
public class HistorialCitaConsultaService {
    @Autowired
    private HistorialCitaViewRepository repository;

    public List<HistorialCitaView> porPaciente(Long pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }

    public java.util.Optional<HistorialCitaView> porReserva(Long reservaId) {
        return repository.findByReservaId(reservaId);
    }
}