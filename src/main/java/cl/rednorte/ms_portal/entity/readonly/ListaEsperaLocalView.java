package cl.rednorte.ms_portal.entity.readonly;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import lombok.Getter;

@Entity
@Table(name = "lista_espera_local")
@Immutable
@Getter
public class ListaEsperaLocalView {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "centro_id")
    private CentroMedicoView centro;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private UsuarioView paciente;

    private Integer prioridad;
}