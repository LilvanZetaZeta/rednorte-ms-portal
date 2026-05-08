package cl.rednorte.ms_portal.entity.readonly;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.util.List;

/**
 * Vista de SOLO LECTURA de la tabla "usuario".
 *
 * Mapea a la MISMA tabla física "usuario" que la entidad
 * cl.rednorte.ms_gestion.entity.Usuario del microservicio ms-gestion.
 *
 * En ms-portal solo necesitamos leer:
 *  - Datos básicos del paciente (para mostrar nombre, correo, RUT en historiales)
 *  - Datos del médico (para mostrar nombre del doctor en agendas)
 *  - Datos del centro médico asignado (para staff filtering)
 *  - Especialidades del médico (para búsquedas)
 *
 * NUNCA escribimos en esta tabla desde ms-portal. Toda mutación
 * (registro, cambio de rol, asignación de centro) sigue siendo
 * responsabilidad exclusiva de ms-gestion.
 */
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

    /**
     * Relación read-only con CentroMedicoView.
     * Necesario para mostrar a qué sucursal pertenece cada miembro del staff
     * en el dashboard del director (DashboardAdmin.tsx).
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "centro_medico_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CentroMedicoView centroMedico;

    /**
     * Relación read-only con la tabla puente usuario_especialidad.
     * Necesario para listar las especialidades de un médico.
     *
     * IMPORTANTE: Aunque sea @ManyToMany, la tabla puente NO se modifica
     * desde ms-portal. Solo lee la asociación tal como la dejó ms-gestion.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_especialidad",
        joinColumns = @JoinColumn(name = "usuario_id", insertable = false, updatable = false),
        inverseJoinColumns = @JoinColumn(name = "especialidad_id", insertable = false, updatable = false)
    )
    private List<EspecialidadView> especialidades;

    protected UsuarioView() {}

    /**
     * Enum DUPLICADO desde ms-gestion. Los valores deben coincidir exactamente
     * con cl.rednorte.ms_gestion.entity.Usuario.RolUsuario.
     */
    public enum RolUsuario { PACIENTE, MEDICO, ADMINISTRATIVO, DIRECTOR, SECRETARIA }
}