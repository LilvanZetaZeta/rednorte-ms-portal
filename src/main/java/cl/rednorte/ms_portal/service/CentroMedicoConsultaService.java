package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.rednorte.ms_portal.entity.readonly.CentroMedicoView;
import cl.rednorte.ms_portal.repository.readonly.CentroMedicoViewRepository;
import java.util.List;

@Service
public class CentroMedicoConsultaService {
    @Autowired private CentroMedicoViewRepository repository;

    public List<CentroMedicoView> listarTodos() { return repository.findAll(); }
    public CentroMedicoView obtenerPorId(Long id) { return repository.findById(id).orElseThrow(() -> new RuntimeException("Centro no encontrado")); }
    public List<CentroMedicoView> buscarPorLocalizacion(String region, String comuna) {
        if (region != null && comuna != null) return repository.findByRegionAndComunaIgnoreCase(region, comuna);
        if (comuna != null) return repository.findByComunaIgnoreCase(comuna);
        return repository.findAll();
    }
}