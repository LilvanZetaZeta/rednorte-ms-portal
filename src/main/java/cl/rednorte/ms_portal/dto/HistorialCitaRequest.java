package cl.rednorte.ms_portal.dto;

import cl.rednorte.ms_portal.entity.HistorialCita.EstadoHistorico;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistorialCitaRequest {
    private Long pacienteId;
    private String detalleProcedimiento;
    private LocalDateTime fechaCita;
    private EstadoHistorico estadoHistorico;
}