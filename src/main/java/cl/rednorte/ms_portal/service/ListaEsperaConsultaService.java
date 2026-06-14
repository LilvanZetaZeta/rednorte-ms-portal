package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.rednorte.ms_portal.entity.readonly.ListaEsperaLocalView;
import cl.rednorte.ms_portal.repository.readonly.ListaEsperaLocalViewRepository;
import java.util.List;

@Service
public class ListaEsperaConsultaService {
    @Autowired private ListaEsperaLocalViewRepository repository;

    public List<ListaEsperaLocalView> listarTodas() { return repository.findAll(); }
    public ListaEsperaLocalView obtenerPorId(Long id) { return repository.findById(id).orElseThrow(() -> new RuntimeException("No encontrado")); }
    public List<ListaEsperaLocalView> obtenerPorPaciente(Long pacienteId) { return repository.findByPacienteId(pacienteId); }
    public List<ListaEsperaLocalView> obtenerPorCentro(Long centroId) { return repository.findByCentroIdOrderByPrioridadAsc(centroId); }
}