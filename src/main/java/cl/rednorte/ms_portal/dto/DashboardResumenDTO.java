package cl.rednorte.ms_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para el endpoint GET /api/portal/metricas/resumen.
 *
 * Contiene los KPIs globales que se muestran en el panel ejecutivo del
 * Director (PortalDirector.tsx) y en el panel del Administrativo Local
 * (DashboardCentro.tsx).
 *
 * IMPORTANTE: La estructura es IDÉNTICA al DashboardResumenDTO original
 * de ms-gestion. Esto es deliberado: el frontend no debe notar que el
 * endpoint cambió de microservicio. El contrato JSON es exactamente el
 * mismo, solo cambia el host/puerto al que apunta el API Gateway.
 *
 * Campos:
 *  - totalReservas:       count(*) sobre la tabla reserva
 *  - reservasVigentes:    count where estado = 'VIGENTE'
 *  - reservasCanceladas:  count where estado = 'CANCELADA'
 *  - totalPacientes:      count where rol = 'PACIENTE'
 *  - totalMedicos:        count where rol = 'MEDICO'
 *  - totalCentros:        count(*) sobre la tabla centro_medico
 *  - totalEspecialidades: count(*) sobre la tabla especialidad
 */
@Data
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