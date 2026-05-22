package cl.rednorte.ms_portal.config;

import cl.rednorte.ms_portal.entity.readonly.UsuarioView;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter
public class RolUsuarioConverter implements AttributeConverter<UsuarioView.RolUsuario, String> {

    @Override
    public String convertToDatabaseColumn(UsuarioView.RolUsuario attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public UsuarioView.RolUsuario convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return UsuarioView.RolUsuario.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Valor desconocido en columna rol_usuario: '" + dbData + "'. " +
                "Valores válidos: PACIENTE, MEDICO, ADMINISTRATIVO, DIRECTOR, SECRETARIA"
            );
        }
    }
}