package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.rednorte.ms_portal.dto.CentroMetricaDTO;
import cl.rednorte.ms_portal.dto.DashboardResumenDTO;
import cl.rednorte.ms_portal.entity.readonly.ReservaView.EstadoReserva;
import cl.rednorte.ms_portal.entity.readonly.UsuarioView.RolUsuario;
import cl.rednorte.ms_portal.repository.readonly.*;
import java.util.List;

@Service
public class MetricasService {
    @Autowired private ReservaViewRepository reservaRepo;
    @Autowired private UsuarioViewRepository usuarioRepo;
    @Autowired private CentroMedicoViewRepository centroRepo;
    @Autowired private EspecialidadViewRepository especialidadRepo;

    public DashboardResumenDTO obtenerResumenGlobal() {
        return DashboardResumenDTO.builder()
            .totalReservas(reservaRepo.count())
            .reservasVigentes(reservaRepo.countByEstado(EstadoReserva.VIGENTE))
            .reservasCanceladas(reservaRepo.countByEstado(EstadoReserva.CANCELADA))
            .totalPacientes(usuarioRepo.countByRol(RolUsuario.PACIENTE))
            .totalMedicos(usuarioRepo.countByRol(RolUsuario.MEDICO))
            .totalCentros(centroRepo.count())
            .totalEspecialidades(especialidadRepo.count())
            .build();
    }

    public List<CentroMetricaDTO> obtenerMetricasPorCentro() { return reservaRepo.obtenerMetricasPorCentro(); }
}