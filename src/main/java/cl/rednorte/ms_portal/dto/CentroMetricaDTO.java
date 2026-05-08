package cl.rednorte.ms_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CentroMetricaDTO {
    private String nombreCentro;
    private long cantidadReservas;
}