package cl.rednorte.ms_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para el endpoint GET /api/portal/metricas/centros.
 *
 * Cada instancia representa el rendimiento operativo de UN centro médico:
 *  - nombreCentro:      nombre legible de la sucursal (ej. "Clínica Norte Central")
 *  - cantidadReservas:  total de reservas asociadas a ese centro
 *
 * Este DTO alimenta directamente el gráfico de barras de "Rendimiento por
 * Sucursal" en PortalDirector.tsx (frontend). Su estructura es IDÉNTICA al
 * CentroMetricaDTO original de ms-gestion para mantener el contrato JSON
 * sin cambios desde la perspectiva del cliente.
 *
 * El service que lo construye (MetricasService) calcula este listado en
 * una única query con GROUP BY (ver ReservaViewRepository.countReservasGroupByCentroId),
 * eliminando el problema N+1 que tenía la implementación de ms-gestion.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CentroMetricaDTO {
    private String nombreCentro;
    private long cantidadReservas;
}