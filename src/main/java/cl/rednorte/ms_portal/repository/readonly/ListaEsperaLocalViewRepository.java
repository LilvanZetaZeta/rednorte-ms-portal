package cl.rednorte.ms_portal.repository.readonly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.rednorte.ms_portal.entity.readonly.ListaEsperaLocalView;
import java.util.List;

@Repository
public interface ListaEsperaLocalViewRepository extends JpaRepository<ListaEsperaLocalView, Long> {
    List<ListaEsperaLocalView> findByCentroIdOrderByPrioridadAsc(Long centroId);
    List<ListaEsperaLocalView> findByPacienteId(Long pacienteId);
}