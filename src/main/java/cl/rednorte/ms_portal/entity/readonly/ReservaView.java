package cl.rednorte.ms_portal.entity.readonly;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Immutable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaView {
    @Id
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private UsuarioView paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private UsuarioView medico;

    @ManyToOne
    @JoinColumn(name = "centro_id")
    private CentroMedicoView centro;

    private LocalDateTime fechaHora;
    
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoReserva estado;
    
    @Enumerated(EnumType.STRING)
    private OrigenReserva origen;

    public enum EstadoReserva { 
    VIGENTE, 
    CONFIRMADA, 
    ATENDIDO, 
    NO_ASISTE, 
    CANCELADA, 
    PENDIENTE_CANCELACION_ADMIN 
    }
    
    public enum OrigenReserva { WEB, PRESENCIAL}
}