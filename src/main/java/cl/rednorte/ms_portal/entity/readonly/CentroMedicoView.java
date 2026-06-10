package cl.rednorte.ms_portal.entity.readonly;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import lombok.*;

@Entity
@Table(name = "centro_medico")
@Immutable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CentroMedicoView {
    @Id
    private Long id;
    private String nombreSucursal;
    private String region;
    private String comuna;
    private String direccion;
}