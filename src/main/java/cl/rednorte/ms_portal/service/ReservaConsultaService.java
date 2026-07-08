package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.repository.readonly.ReservaViewRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservaConsultaService {
    @Autowired
    private ReservaViewRepository repository;

    public List<ReservaView> listarTodas() {
        return repository.findAll();
    }

    public ReservaView obtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    public List<ReservaView> porPacienteIdAuth(String idAuth) {
        return repository.findByPaciente_IdAuth(idAuth);
    }

    public Page<ReservaView> porCentro(Long centroId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaHora").descending());
        return repository.findByCentroId(centroId, pageable);
    }

    public Page<ReservaView> porMedico(Long medicoId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaHora").descending());
        return repository.findByMedicoId(medicoId, pageable);
    }

    public List<String> obtenerSlotsDisponibles(Long medicoId, LocalDate fecha) {
        List<Timestamp> ocupadas = repository.findHorasOcupadasByMedicoAndFecha(medicoId, fecha.toString());

        Set<LocalTime> horasOcupadas = ocupadas.stream()
                .map(ts -> ts.toLocalDateTime().toLocalTime())
                .collect(Collectors.toSet());

        List<String> disponibles = new ArrayList<>();
        LocalTime cursor = LocalTime.of(8, 0);
        LocalTime fin    = LocalTime.of(20, 0);

        while (cursor.isBefore(fin)) {
            if (!horasOcupadas.contains(cursor)) {
                disponibles.add(LocalDateTime.of(fecha, cursor).toString());
            }
            cursor = cursor.plusMinutes(20);
        }

        return disponibles;
    }
}