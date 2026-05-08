package cl.rednorte.ms_portal.entity.readonly;

import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Immutable
@Getter
@Table(name = "reserva")
public class ReservaView {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private UsuarioView paciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private UsuarioView medico;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private CentroMedicoView centro;

    @Column(name = "fecha_hora", insertable = false, updatable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "origen", insertable = false, updatable = false, columnDefinition = "origen_reserva")
    private OrigenReserva origen;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", insertable = false, updatable = false, columnDefinition = "estado_reserva")
    private EstadoReserva estado;

    protected ReservaView() {
    }

    public enum OrigenReserva {
        WEB, PRESENCIAL
    }

    public enum EstadoReserva {
        VIGENTE, CANCELADA
    }
}