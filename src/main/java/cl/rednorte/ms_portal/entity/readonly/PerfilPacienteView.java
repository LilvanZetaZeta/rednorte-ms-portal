package cl.rednorte.ms_portal.entity.readonly;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import lombok.Getter;

@Entity
@Table(name = "perfil_paciente")
@Immutable
@Getter
public class PerfilPacienteView {
    @Id
    private Long id;
    
    @Column(name = "paciente_id")
    private Long pacienteId;
    
    @Column(name = "id_auth")
    private String idAuth;
    
    private String prevision;
    private String telefonoContacto;
}