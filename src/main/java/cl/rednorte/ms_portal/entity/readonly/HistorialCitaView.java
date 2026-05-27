package cl.rednorte.ms_portal.entity.readonly;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import lombok.Getter;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_cita")
@Immutable
@Getter
public class HistorialCitaView {
    @Id
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private UsuarioView paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private UsuarioView medico;

    private LocalDateTime fechaAtencion;
    private String observaciones;
    private String procedimientoRealizado;
}