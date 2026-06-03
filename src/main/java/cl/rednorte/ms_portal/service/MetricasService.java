package cl.rednorte.ms_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.rednorte.ms_portal.dto.CentroMetricaDTO;
import cl.rednorte.ms_portal.dto.DashboardResumenDTO;
import cl.rednorte.ms_portal.dto.DashboardSecretariaDTO;
import cl.rednorte.ms_portal.entity.readonly.ReservaView.EstadoReserva;
import cl.rednorte.ms_portal.entity.readonly.UsuarioView.RolUsuario;
import cl.rednorte.ms_portal.repository.readonly.*;
import java.time.LocalDate;
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

    public List<CentroMetricaDTO> obtenerMetricasPorCentro() { 
        return reservaRepo.obtenerMetricasPorCentro(); 
    }

    public DashboardSecretariaDTO obtenerDashboardSecretaria(Long centroId, LocalDate fecha) {
        try {
            Object[] conteos = reservaRepo.obtenerConteosDashboardSecretaria(centroId, fecha);
            Long totalReservasHoy = 0L;
            Long citasVigentes = 0L;
            Long citasConfirmadas = 0L;
            Long pendientesCheckin = 0L;
            Long citasCanceladasHoy = 0L;
            
            if (conteos != null && conteos.length > 0 && conteos[0] != null) {
                totalReservasHoy = ((Number) conteos[0]).longValue();
            }
            if (conteos != null && conteos.length > 1 && conteos[1] != null) {
                citasVigentes = ((Number) conteos[1]).longValue();
            }
            if (conteos != null && conteos.length > 2 && conteos[2] != null) {
                citasConfirmadas = ((Number) conteos[2]).longValue();
            }
            if (conteos != null && conteos.length > 3 && conteos[3] != null) {
                pendientesCheckin = ((Number) conteos[3]).longValue();
            }
            if (conteos != null && conteos.length > 4 && conteos[4] != null) {
                citasCanceladasHoy = ((Number) conteos[4]).longValue();
            }
            
            Long totalMedicosCentro = reservaRepo.countMedicosByCentroId(centroId);
            if (totalMedicosCentro == null) totalMedicosCentro = 0L;
            
            return DashboardSecretariaDTO.builder()
                .totalReservasHoy(totalReservasHoy)
                .citasVigentes(citasVigentes)
                .citasConfirmadas(citasConfirmadas)
                .pendientesCheckin(pendientesCheckin)
                .citasCanceladasHoy(citasCanceladasHoy)
                .totalMedicosCentro(totalMedicosCentro)
                .build();
        } catch (Exception e) {
            System.err.println("Error dashboard: " + e.getMessage());
            return DashboardSecretariaDTO.builder()
                .totalReservasHoy(0L)
                .citasVigentes(0L)
                .citasConfirmadas(0L)
                .pendientesCheckin(0L)
                .citasCanceladasHoy(0L)
                .totalMedicosCentro(0L)
                .build();
        }
    }
}
