package cl.rednorte.ms_portal.entity.readonly;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * Vista de SOLO LECTURA de la tabla "centro_medico".
 *
 * Mapea a la MISMA tabla física "centro_medico" que la entidad
 * cl.rednorte.ms_gestion.entity.CentroMedico del microservicio ms-gestion.
 *
 * En ms-portal usamos esta vista para:
 *  - Mostrar el nombre de la sucursal en historiales de citas del paciente
 *  - Listar centros en el panel de métricas del director
 *  - Mostrar a qué centro pertenece cada miembro del staff
 *
 * El ABM completo de centros médicos (crear, editar, eliminar sucursales)
 * sigue siendo responsabilidad exclusiva de ms-gestion vía
 * CentroMedicoController + CentroMedicoService.
 */
@Entity
@Immutable
@Getter
@Table(name = "centro_medico")
public class CentroMedicoView {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "nombre_sucursal", insertable = false, updatable = false)
    private String nombreSucursal;

    @Column(name = "region", insertable = false, updatable = false)
    private String region;

    @Column(name = "comuna", insertable = false, updatable = false)
    private String comuna;

    @Column(name = "direccion", insertable = false, updatable = false)
    private String direccion;

    protected CentroMedicoView() {}
}