package cl.rednorte.ms_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResumenDTO {
    private long totalReservas;
    private long reservasVigentes;
    private long reservasCanceladas;
    private long totalPacientes;
    private long totalMedicos;
    private long totalCentros;
    private long totalEspecialidades;
}