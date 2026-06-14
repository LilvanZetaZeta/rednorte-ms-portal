package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.rednorte.ms_portal.entity.readonly.EspecialidadView;
import cl.rednorte.ms_portal.repository.readonly.EspecialidadViewRepository;
import java.util.List;

@Service
public class EspecialidadConsultaService {
    @Autowired private EspecialidadViewRepository repository;

    public List<EspecialidadView> listar() { return repository.findAll(); }
    public EspecialidadView obtenerPorId(Long id) { return repository.findById(id).orElseThrow(() -> new RuntimeException("Especialidad no encontrada")); }
}