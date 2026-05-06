package cl.rednorte.ms_portal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historial_citas")
public class HistorialCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "detalle_procedimiento", nullable = false, length = 500)
    private String detalleProcedimiento;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDateTime fechaCita;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_historico", nullable = false, length = 20)
    private EstadoHistorico estadoHistorico;

    public enum EstadoHistorico {
        FINALIZADA, CANCELADA
    }
}