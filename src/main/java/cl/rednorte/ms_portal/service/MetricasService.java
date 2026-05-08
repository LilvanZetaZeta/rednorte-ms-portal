package cl.rednorte.ms_portal.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.rednorte.ms_portal.dto.CentroMetricaDTO;
import cl.rednorte.ms_portal.dto.DashboardResumenDTO;
import cl.rednorte.ms_portal.entity.readonly.CentroMedicoView;
import cl.rednorte.ms_portal.entity.readonly.ReservaView;
import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import cl.rednorte.ms_portal.repository.readonly.CentroMedicoViewRepository;
import cl.rednorte.ms_portal.repository.readonly.EspecialidadViewRepository;
import cl.rednorte.ms_portal.repository.readonly.ReservaViewRepository;
import cl.rednorte.ms_portal.repository.readonly.UsuarioViewRepository;

@Service
@Transactional(readOnly = true)
public class MetricasService {

    @Autowired
    private ReservaViewRepository reservaRepo;
    @Autowired
    private UsuarioViewRepository usuarioRepo;
    @Autowired
    private CentroMedicoViewRepository centroRepo;
    @Autowired
    private EspecialidadViewRepository especialidadRepo;

    public DashboardResumenDTO obtenerResumen() {
        return new DashboardResumenDTO(
                reservaRepo.count(),
                reservaRepo.countByEstadoNative(ReservaView.EstadoReserva.VIGENTE.name()),
                reservaRepo.countByEstadoNative(ReservaView.EstadoReserva.CANCELADA.name()),
                usuarioRepo.countByRolNative(UsuarioView.RolUsuario.PACIENTE.name()),
                usuarioRepo.countByRolNative(UsuarioView.RolUsuario.MEDICO.name()),
                centroRepo.count(),
                especialidadRepo.count());
    }

    public List<CentroMetricaDTO> obtenerMetricasPorCentro() {
        List<CentroMedicoView> centros = centroRepo.findAll();

        List<Object[]> conteos = reservaRepo.countReservasGroupByCentroId();

        Map<Long, Long> conteoPorCentroId = conteos.stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> ((Number) row[1]).longValue()));

        return centros.stream()
                .map(c -> new CentroMetricaDTO(
                        c.getNombreSucursal(),
                        conteoPorCentroId.getOrDefault(c.getId(), 0L)))
                .collect(Collectors.toList());
    }
}