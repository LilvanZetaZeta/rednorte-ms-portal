package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    
    // ✅ PAGINACIÓN: Se agregó Pageable para evitar traer miles de registros
    public Page<ReservaView> porCentro(Long centroId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaHora").descending());
        return repository.findByCentroId(centroId, pageable);
    }
    
    public Page<ReservaView> porMedico(Long medicoId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaHora").descending());
        return repository.findByMedicoId(medicoId, pageable);
    }
}
