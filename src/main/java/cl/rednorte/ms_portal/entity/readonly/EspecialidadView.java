package cl.rednorte.ms_portal.entity.readonly;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import lombok.Getter;

@Entity
@Table(name = "especialidad")
@Immutable
@Getter
public class EspecialidadView {
    @Id
    private Long id;
    private String nombre;
}