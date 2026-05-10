package cl.rednorte.ms_portal.entity.readonly;

import java.util.List;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Immutable
@Getter
@Table(name = "usuario")
public class UsuarioView {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "id_auth", insertable = false, updatable = false)
    private String idAuth;

    @Column(name = "rut", insertable = false, updatable = false)
    private String rut;

    @Column(name = "nombre_completo", insertable = false, updatable = false)
    private String nombreCompleto;

    @Column(name = "correo", insertable = false, updatable = false)
    private String correo;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", insertable = false, updatable = false, columnDefinition = "rol_usuario")
    private RolUsuario rol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_medico_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private CentroMedicoView centroMedico;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_especialidad", joinColumns = @JoinColumn(name = "usuario_id", insertable = false, updatable = false), inverseJoinColumns = @JoinColumn(name = "especialidad_id", insertable = false, updatable = false))
    private List<EspecialidadView> especialidades;

    protected UsuarioView() {
    }

    public enum RolUsuario {
        PACIENTE, MEDICO, ADMINISTRATIVO, DIRECTOR, SECRETARIA
    }
}