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
@Table(name = "especialidad")
public class EspecialidadView {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "nombre", insertable = false, updatable = false)
    private String nombre;

    protected EspecialidadView() {
    }
}