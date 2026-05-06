package cl.rednorte.ms_portal.dto;

import lombok.Data;

@Data
public class PerfilPacienteRequest {
    private Long pacienteId;
    private String prevision;
    private String telefonoContacto;
}