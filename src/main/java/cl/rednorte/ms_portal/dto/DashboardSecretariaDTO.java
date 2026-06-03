package cl.rednorte.ms_portal.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSecretariaDTO {
    private Long totalReservasHoy;
    private Long citasVigentes;
    private Long citasConfirmadas;
    private Long pendientesCheckin;
    private Long citasCanceladasHoy;
    private Long totalMedicosCentro;
}