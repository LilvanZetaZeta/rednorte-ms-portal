package cl.rednorte.ms_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CentroMetricaDTO {
    private String nombreCentro;
    private long cantidadReservas;
}