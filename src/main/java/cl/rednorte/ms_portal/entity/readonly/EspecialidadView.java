package cl.rednorte.ms_portal.entity.readonly;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * Vista de SOLO LECTURA de la tabla "especialidad".
 *
 * Mapea a la MISMA tabla física "especialidad" que la entidad
 * cl.rednorte.ms_gestion.entity.Especialidad del microservicio ms-gestion.
 *
 * En ms-portal usamos esta vista para:
 *  - Mostrar las especialidades de un médico en su perfil/agenda
 *  - Resolver nombres de especialidad en consultas analíticas
 *    (ej: "¿cuántas reservas tuvo Cardiología este mes?")
 *  - Listar especialidades disponibles en pantallas informativas
 *
 * El ABM de especialidades (crear, renombrar, eliminar) sigue siendo
 * responsabilidad exclusiva de ms-gestion vía EspecialidadController.
 */
@Entity
@Immutable
@Getter
@Table(name = "especialidad")
public class EspecialidadView {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "nombre", insertable = false, updatable = false)
    private String nombre;

    protected EspecialidadView() {}
}