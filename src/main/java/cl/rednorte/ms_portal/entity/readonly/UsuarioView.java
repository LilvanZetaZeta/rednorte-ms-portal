package cl.rednorte.ms_portal.entity.readonly;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import lombok.Getter;
import java.util.List;

@Entity
@Table(name = "usuario")
@Immutable
@Getter
public class UsuarioView {
    @Id
    private Long id;
    private String idAuth;
    private String rut;
    private String nombreCompleto;
    private String correo;
    
    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_especialidad", 
        joinColumns = @JoinColumn(name = "usuario_id"), 
        inverseJoinColumns = @JoinColumn(name = "especialidad_id"))
    private List<EspecialidadView> especialidades;

    @ManyToOne
    @JoinColumn(name = "centro_medico_id")
    private CentroMedicoView centroMedico;

    public enum RolUsuario { DIRECTOR, ADMINISTRATIVO, SECRETARIA, MEDICO, PACIENTE }
}