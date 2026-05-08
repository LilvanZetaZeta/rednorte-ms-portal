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

/**
 * Vista de SOLO LECTURA de la tabla "reserva".
 *
 * IMPORTANTE: Esta entidad es propiedad de ms-gestion (escritura).
 * ms-portal SOLO la lee. Por eso aplicamos varias capas de defensa:
 *
 *  1. @Immutable: Hibernate ignora cualquier UPDATE/INSERT sobre esta entidad
 *     en el flush. Aunque alguien llame save(), no se persiste.
 *  2. insertable=false, updatable=false en cada columna: a nivel JPA, ninguna
 *     columna puede ser escrita.
 *  3. Solo @Getter de Lombok. Cero setters, cero @Data.
 *  4. Constructor protected: no se instancia desde código de aplicación,
 *     solo Hibernate la construye al leer la BD.
 *
 * Mapea a la MISMA tabla física "reserva" que la entidad
 * cl.rednorte.ms_gestion.entity.Reserva del microservicio ms-gestion.
 */
@Entity
@Immutable
@Getter
@Table(name = "reserva")
public class ReservaView {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    /**
     * Relación con Usuario (paciente). Read-only.
     * No usamos @OneToMany inverso para evitar acoplamiento.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuarioView paciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuarioView medico;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CentroMedicoView centro;

    @Column(name = "fecha_hora", insertable = false, updatable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "origen", insertable = false, updatable = false, columnDefinition = "origen_reserva")
    private OrigenReserva origen;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", insertable = false, updatable = false, columnDefinition = "estado_reserva")
    private EstadoReserva estado;

    /**
     * Constructor protegido — solo Hibernate lo invoca via reflexión.
     * Bloquea instanciación manual desde código de aplicación.
     */
    protected ReservaView() {}

    // Enums DUPLICADOS desde ms-gestion. NO los importamos del otro microservicio
    // porque eso crearía un acoplamiento de código fuerte. Los valores deben
    // coincidir exactamente con cl.rednorte.ms_gestion.entity.Reserva.
    public enum OrigenReserva { WEB, PRESENCIAL }
    public enum EstadoReserva { VIGENTE, CANCELADA }
}