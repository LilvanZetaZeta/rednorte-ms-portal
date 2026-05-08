package cl.rednorte.ms_portal.entity.readonly;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

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

    protected CentroMedicoView() {
    }
}